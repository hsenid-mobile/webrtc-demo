/*
*  (C) Copyright 2010-2015 hSenid Mobile Solutions (Pvt) Limited.
*  All Rights Reserved.
*
*  These materials are unpublished, proprietary, confidential source code of
*  hSenid Mobile Solutions (Pvt) Limited and constitute a TRADE SECRET
*  of hSenid Mobile Solutions (Pvt) Limited.
*
*  hSenid Mobile Solutions (Pvt) Limited retains all title to and intellectual
*  property rights in these materials.
*/


package hms.webrtc.demo.controller;


import hms.tap.api.WebRTCApi;
import hms.webrtc.demo.controller.bean.AdItemForm;
import hms.webrtc.demo.controller.bean.AdvertisementType;
import hms.webrtc.demo.domain.AdItem;
import hms.webrtc.demo.service.AdItemService;
import hms.webrtc.demo.util.ResponseCode;
import hms.webrtc.demo.util.ResponseKey;
import hms.webrtc.demo.util.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private static final String FILE_URL = "http://127.0.0.1:8080/images/";
    private static final String FILE_UPLOAD_LOCATION = "../webapps/images/";
    private String uploadPosterURL;

    @Autowired
    private AdItemService adItemService;

    @Value("${msisdn.pattern.one}")
    public String msisdnPatternOne;

    @Value("${msisdn.pattern.two}")
    public String msisdnPatternTwo;

    @Value("${code.one}")
    public String codeOne;

    @Value("${code.two}")
    public String codeTwo;

    @Value("${app.id}")
    public String appId;

    @Value("${app.password}")
    public String appPassword;

    @Value("${create.component.url}")
    public String createComponentUrl;

    @Value("${request.script.url}")
    public String requestScriptUrl;

    private WebRTCApi webRTCApi = new WebRTCApi();

    @RequestMapping(value = "/listAds", method = RequestMethod.GET)
    public ModelAndView homePage(ModelMap model) {
        logger.debug("Request Received");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("adItemList", adItemService.getAllAdItems());
        modelAndView.setViewName("listAllAds");
        return modelAndView;
    }

    @RequestMapping(value = "/createAd", method = RequestMethod.GET)
    public ModelAndView createAd(ModelMap model, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        logger.debug("Page [createAd]");
        AdItemForm adItemForm = new AdItemForm();
        modelAndView.addObject("adItemForm", adItemForm);
        modelAndView.addObject("advertisementTypes", AdvertisementType.values());
        modelAndView.setViewName("createAdItem");
        return modelAndView;
    }

    @RequestMapping(value = "/createAd", method = RequestMethod.POST)
    public ModelAndView submitAd(@ModelAttribute("adItemForm") AdItemForm adItemForm, BindingResult result, ModelMap model) {
        logger.debug("AdItemForm form request > " + adItemForm.toString());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("advertisementTypes", AdvertisementType.values());
        modelAndView.addObject("adItemForm", adItemForm);
        modelAndView.setViewName("createAdItem");

        String adId = UUID.randomUUID().toString();

        if (!result.hasErrors()) {

            Map<String, Object> componentResp =  webRTCApi.createComponent(createComponentUrl, appId, appPassword, adId, formatMobileNumber(adItemForm.getMobileNumber()));
            if (ResponseCode.S1000.name().equals(componentResp.get(ResponseKey.STATUS_CODE))) {
                Map<String, Object> requestScriptResp =   webRTCApi.requestScript(requestScriptUrl, appId, appPassword, adId);

                if (ResponseCode.S1000.name().equals(requestScriptResp.get(ResponseKey.STATUS_CODE))) {
                    String requestedScript = (String)requestScriptResp.get(ResponseKey.SCRIPT);

                    boolean isItemCreatedSuccessfully
                            = adItemService.saveAdItem(adItemForm, adId, requestedScript, uploadPosterURL, formatMobileNumber(adItemForm.getMobileNumber()));
                    if (isItemCreatedSuccessfully) {
                        modelAndView.addObject("successMessage", "You have Successfully Created the Ad unit.");
                    } else {
                        modelAndView.addObject("errorMessage", "Error occurred while processing.");
                    }
                }

            }
        }

        return modelAndView;
    }


    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public
    @ResponseBody
    List<UploadedFile> addFile(MultipartHttpServletRequest request) throws IOException {

        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf = request.getFile(itr.next());
        String fileName = mpf.getOriginalFilename();

        Map<String, MultipartFile> fileMap = request.getFileMap();
        List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();

        try {
            byte[] fileContent = mpf.getBytes();
            setUploadPosterURL(FILE_URL + fileName);
            createFile(fileName, fileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (MultipartFile multipartFile : fileMap.values()) {
            UploadedFile fileInfo = getUploadedFileInfo(multipartFile);
            // adding the file info to the list
            uploadedFiles.add(fileInfo);
        }

        logger.debug("Returning Files >> " + uploadedFiles);
        return uploadedFiles;
    }

    private UploadedFile getUploadedFileInfo(MultipartFile multipartFile) throws IOException {
        UploadedFile fileInfo = new UploadedFile();
        fileInfo.setName(multipartFile.getOriginalFilename());
        fileInfo.setSize(multipartFile.getSize());
        fileInfo.setType(multipartFile.getContentType());
        fileInfo.setLocation(FILE_UPLOAD_LOCATION);
        return fileInfo;
    }

    public void createFile(String file, byte[] content) {
        try {
            final File f = new File(FILE_UPLOAD_LOCATION + file);
            if (f.exists()) {
                logger.debug("[FS] [Create] Unable to create file [" + file + "]. File already exists");
            }
            createParentPathIfNotExists(f.getParentFile());
            logger.debug("[FS] [Create] File created  [" + f.toPath() + "]");
            Files.write(f.toPath(), content, StandardOpenOption.CREATE_NEW);
            logger.debug("[FS] [Create] File created successfully [" + file + "]");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createParentPathIfNotExists(File f) throws FileNotFoundException {
        if (null == f || f.exists()) {
            if (f.isDirectory()) {
                return;
            } else {
                throw new FileNotFoundException();
            }
        }
        createParentPathIfNotExists(f.getParentFile());
        f.mkdir();
    }


    private String formatMobileNumber(String mobileNumber) {
        Pattern patternOne = createPattern(msisdnPatternOne);
        Pattern patternTwo = createPattern(msisdnPatternTwo);
        if (isMatching(mobileNumber, patternOne)) {
            return codeOne + mobileNumber;
        } else if (isMatching(mobileNumber, patternTwo)) {
            return codeTwo + mobileNumber.replaceFirst("0", "");
        } else {
            return codeTwo + mobileNumber;
        }
    }

    public Pattern createPattern(String patternText) {
        return Pattern.compile(patternText);
    }

    public boolean isMatching(String data, Pattern pattern) {
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }

    public String getUploadPosterURL() {
        return uploadPosterURL;
    }

    public void setUploadPosterURL(String uploadPosterURL) {
        this.uploadPosterURL = uploadPosterURL;
    }
}
