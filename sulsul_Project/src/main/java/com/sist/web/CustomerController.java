package com.sist.web;
import com.sist.vo.*; 
import com.sist.dao.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {
	@Autowired
	private InformationDAO idao;
	@Autowired
	private FaqDAO fdao;
	@Autowired
	private AskDAO adao;
	
	//faq관련 내용
	@GetMapping("customer/faq.do")
	public String customerservice_faq(FaqCategoryVO vo,Model model)
	{
		// 나라별 센터 소개
		List<NationIntroduceVO> nList=fdao.nationListData();
		// faq 카테고리
		List<FaqCategoryVO> fList=fdao.FaqCategoryList();
		// faq 리스트 출력
		List<FaqVO> faqList=fdao.FaqListData(vo.getFcno());
		
		model.addAttribute("faqList",faqList);
		model.addAttribute("nList",nList);
		model.addAttribute("fList",fList);
		
		return "customer/customer_faq";
	}

	
	//////////////////////////////////////////////////////////
	
	//이용 안내 관련 내용
	@GetMapping("customer/iu.do")
	public String customerservice_introduce(String page,Model model)
	{
		if(page==null)
			page="1";
		int curpage=Integer.parseInt(page);
		
		Map map=new HashMap();
		int start=(curpage*10)-9;
		int end=(curpage*10);
		map.put("start",start);
		map.put("end",end);
		
		List<InformationUseVO> list=idao.informationListData(map);
		int totalpage=idao.informationTotalPage();
		
		model.addAttribute("list",list);
		model.addAttribute("curpage",curpage);
		model.addAttribute("totalpage",totalpage);
		return "customer/customer_infomation";
	}
	
	// 이용안내 상세페이지 전환
	@GetMapping("customer/iuDetail.do")
	public String customerservice_introduce_detail(int iuno,Model model)
	{
		InformationUseVO vo=idao.informationDetailData(iuno);
		String image=vo.getImage();
		// TO_DO : 이미지가 여러개일경우 여러개 출력되게 하기
		List<String> list=new ArrayList<String>();
		if(!image.contains(","))
		{
			list.add(image);
		}
		else 
		{
			String[] images=image.split(",");
			for(int i=0; i<images.length-1;i++)
			 {
				 list.add(images[i]);
			 }
		}
				
		model.addAttribute("list",list);
		model.addAttribute("vo",vo);
		model.addAttribute("iuno",iuno);
		return "customer/customer_information_detail";
	}
	
	
	
	
	/////////////////////////////////////////////////////////////
	//1:1 문의관련 내용
	@GetMapping("customer/ask.do")
	public String customerservice_ask(HttpSession session,Model model)
	{
		String id=(String)session.getAttribute("id");
		System.out.println("컨트롤러 아이디"+id);
		int count=0;
		if(id==null)
		{
			count=adao.asknotTotalData();
		}
		else
		{
		    count =adao.askTotalData(id);
		}
		model.addAttribute("count",count);
		System.out.println("모델"+count);
		return "customer/customer_ask";
	}
	
	@GetMapping("customer/ask_insert.do")
	public String ask_insert()
	{
		return "customer/ask_insert";
	}
	
	@GetMapping("customer/ask_detail.do")
	public String ask_detail(int ano,Model model)
	{
		model.addAttribute("ano",ano);
		return "customer/ask_detail";
	}
	
	@GetMapping("customer/ask_update.do")
	public String ask_update(int ano,Model model)
	{
		model.addAttribute("ano",ano);
		return "customer/ask_update";
	}
	
	@GetMapping("customer/ask_delete.do")
	public String ask_delete(int ano,Model model)
	{
		model.addAttribute("ano",ano);
		return "customer/ask_delete";
	}
	
	
}