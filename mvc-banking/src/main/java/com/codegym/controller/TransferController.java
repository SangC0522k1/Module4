package com.codegym.controller;

import com.codegym.model.Transfer;
import com.codegym.service.Transfer.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/transfers")
public class TransferController {
    @Autowired
    private TransferService transferService;
     @GetMapping
    public ModelAndView showlisthitory(){
         ModelAndView modelAndView = new ModelAndView();
         modelAndView.setViewName("transfer-history/index");

         Iterable<Transfer> transfers = transferService.findAll();
         BigDecimal getAllFeesAmount = transferService.getSumFeesAmount();

         modelAndView.addObject("transfers", transfers);
         modelAndView.addObject("allFeesAmount", getAllFeesAmount);

         return modelAndView;
     }
}
