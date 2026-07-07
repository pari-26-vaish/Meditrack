package com.meditrack.controller;

import com.meditrack.model.Medicine;
import com.meditrack.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private MedicineRepository medicineRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Medicine> medicines = medicineRepository.findAll();
        model.addAttribute("medicines", medicines);
        return "index";
    }
}