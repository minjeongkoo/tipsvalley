package com.tips.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tips.back.common.RestResponseEntity;
import com.tips.back.model.json.MeasureInfoRealJsonList;
import com.tips.back.service.MeasureInfoRealEntityService;

@RestController
@RequestMapping("/")
public class BackController
{
    @Autowired
    private MeasureInfoRealEntityService measureInfoRealEntityService;
    
    @GetMapping("/list")
    public RestResponseEntity<MeasureInfoRealJsonList> findMeasureInfoRealAll()
    {
        RestResponseEntity<MeasureInfoRealJsonList> result = null;
        
        result = new RestResponseEntity<MeasureInfoRealJsonList>(this.measureInfoRealEntityService.findMeasureInfoRealEntity());
        
        return result;
    }

    /*
     * URL : /list/all?sido_name=<sido_name>&mang_name=<mang_name>
     * Parameter
     * - sindo_name(optional)
     * 		# 서울
     * 		# 경기
     * 		# 인천
     * - mang_name(optional)
     * 		# 도시대기
     * 		# 도로변대기
     */
    @GetMapping("/list/all")
    public RestResponseEntity<MeasureInfoRealJsonList> findMeasureInfoReal(
    		@RequestParam(value="sido_name", required=false) String sidoName, 
    		@RequestParam(value="mang_name", required=false) String mangName)
    {
        RestResponseEntity<MeasureInfoRealJsonList> result = null;
        
        result = new RestResponseEntity<MeasureInfoRealJsonList>(this.measureInfoRealEntityService.findMeasureInfoRealEntity(sidoName, mangName));
        
        return result;
    }
    
    /*
     * URL : /list/page?page=<page>&size=<size>&sort=<property>,<asc|desc>
     * Parameter
     * - page : sequence of page that start from 0
     * - size : num of data
     * - property
     * 		# id : pk
     * 		# datatime
     * 		# ...
     */   
    @GetMapping("/list/page")
    public RestResponseEntity<MeasureInfoRealJsonList> findMeasureInfoRealPage(Pageable pageable)
    {
    	RestResponseEntity<MeasureInfoRealJsonList> result = null;
    	
    	result = new RestResponseEntity<MeasureInfoRealJsonList>(this.measureInfoRealEntityService.findMeasureInfoRealEntity(pageable));
    	
    	return result;
    }
    
    /*
     * URL : /list/so2?sido_name=<sido_name>&mang_name=<mang_name>
     * Parameter
     * - sindo_name(optional)
     * 		# 서울
     * 		# 경기
     * 		# 인천
     * - mang_name(optional)
     * 		# 도시대기
     * 		# 도로변대기
     */
    @GetMapping("/list/so2")
    public RestResponseEntity<MeasureInfoRealJsonList> findSO2ValueInfoReal(
    		@RequestParam(value="sido_name", required=false) String sidoName, 
    		@RequestParam(value="mang_name", required=false) String mangName)
    {
    	RestResponseEntity<MeasureInfoRealJsonList> result = null;
    	
		result = new RestResponseEntity<MeasureInfoRealJsonList>(this.measureInfoRealEntityService.findSO2ValueInfoRealEntity(sidoName, mangName));
    	
    	return result;
    }
    
    /*
     * URL : /list/khai?sido_name=<sido_name>&mang_name=<mang_name>
     * Parameter
     * - sindo_name(optional)
     * 		# 서울
     * 		# 경기
     * 		# 인천
     * - mang_name(optional)
     * 		# 도시대기
     * 		# 도로변대기
     */
    @GetMapping("/list/khai")
    public RestResponseEntity<MeasureInfoRealJsonList> findKhaiValueInfoReal(
    		@RequestParam(value="sido_name", required=false) String sidoName, 
    		@RequestParam(value="mang_name", required=false) String mangName)
    {
    	RestResponseEntity<MeasureInfoRealJsonList> result = null;
    	
    	result = new RestResponseEntity<MeasureInfoRealJsonList>(this.measureInfoRealEntityService.findKhaiValueInfoRealEntity(sidoName, mangName));
    	
    	return result;
    }    
}