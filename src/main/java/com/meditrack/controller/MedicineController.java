package com.meditrack.controller;

import com.meditrack.model.DoseLog;
import com.meditrack.model.Medicine;
import com.meditrack.repository.DoseLogRepository;
import com.meditrack.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/medicines")
public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private DoseLogRepository doseLogRepository;

    // ---- Add ----
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        return "add-medicine";
    }

    @PostMapping("/add")
    public String saveMedicine(@ModelAttribute Medicine medicine) {
        medicineRepository.save(medicine);
        return "redirect:/medicines";
    }

    // ---- List ----
    @GetMapping
    public String listMedicines(Model model) {
        model.addAttribute("medicines", medicineRepository.findAll());
        return "medicine-list";
    }

    // ---- Edit ----
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow();
        model.addAttribute("medicine", medicine);
        return "edit-medicine";
    }

    @PostMapping("/edit/{id}")
    public String updateMedicine(@PathVariable Long id, @ModelAttribute Medicine updated) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow();
        medicine.setName(updated.getName());
        medicine.setDosage(updated.getDosage());
        medicine.setReminderTime(updated.getReminderTime());
        medicine.setFrequency(updated.getFrequency());
        medicine.setNotes(updated.getNotes());
        medicine.setIcon(updated.getIcon());
        medicine.setPillsRemaining(updated.getPillsRemaining());
        medicineRepository.save(medicine);
        return "redirect:/medicines";
    }

    // ---- Delete (also removes related dose logs first) ----
    @PostMapping("/delete/{id}")
    public String deleteMedicine(@PathVariable Long id) {
        Medicine medicine = medicineRepository.findById(id).orElse(null);
        if (medicine != null) {
            List<DoseLog> logs = doseLogRepository.findByMedicineId(id);
            doseLogRepository.deleteAll(logs);
            medicineRepository.delete(medicine);
        }
        return "redirect:/medicines";
    }

    // ---- Mark dose status ----
    @PostMapping("/{id}/mark")
    public String markDose(@PathVariable Long id, @RequestParam String status) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow();
        DoseLog log = new DoseLog(medicine, LocalDate.now(), status);
        doseLogRepository.save(log);
        return "redirect:/medicines";
    }

    // ---- Stats / Health History chart ----
    @GetMapping("/stats")
    public String stats(Model model) {
        List<Medicine> medicines = medicineRepository.findAll();
        Map<String, Object> statsMap = new HashMap<>();

        for (Medicine med : medicines) {
            List<DoseLog> logs = doseLogRepository.findByMedicineId(med.getId());
            long taken = logs.stream().filter(l -> l.getStatus().equals("TAKEN")).count();
            long total = logs.size();
            double percent = total == 0 ? 0 : (taken * 100.0 / total);
            statsMap.put(med.getName(), Math.round(percent));
        }

        model.addAttribute("medicines", medicines);
        model.addAttribute("statsMap", statsMap);
        return "stats";
    }

    // ---- Dashboard ----
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Medicine> medicines = medicineRepository.findAll();
        List<DoseLog> allLogs = doseLogRepository.findAll();

        long totalMedicines = medicines.size();
        long takenToday = allLogs.stream()
                .filter(l -> l.getDate().equals(LocalDate.now()) && l.getStatus().equals("TAKEN"))
                .count();
        long missedToday = allLogs.stream()
                .filter(l -> l.getDate().equals(LocalDate.now()) && l.getStatus().equals("MISSED"))
                .count();

        long totalLogs = allLogs.size();
        long totalTaken = allLogs.stream().filter(l -> l.getStatus().equals("TAKEN")).count();
        double overallAdherence = totalLogs == 0 ? 0 : Math.round(totalTaken * 100.0 / totalLogs);

        model.addAttribute("totalMedicines", totalMedicines);
        model.addAttribute("takenToday", takenToday);
        model.addAttribute("missedToday", missedToday);
        model.addAttribute("overallAdherence", overallAdherence);
        model.addAttribute("medicines", medicines);

        return "dashboard";
    }

    // ---- History (full log) ----
    @GetMapping("/history")
    public String history(Model model) {
        List<DoseLog> logs = doseLogRepository.findAll();
        logs.sort((a, b) -> b.getRecordedAt().compareTo(a.getRecordedAt()));
        model.addAttribute("logs", logs);
        return "history";
    }
}