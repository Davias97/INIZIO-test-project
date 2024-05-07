package com.example.iniziotestproject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Controller
public class GoogleParserController {

    private List<String> titles;

    @GetMapping("/parse")
    public ModelAndView parseForm() {
        ModelAndView mav = new ModelAndView("index");
        if (titles != null && titles.size() > 2) {
            mav.addObject("titles", titles.subList(0, titles.size() - 2));
        } else {
            mav.addObject("titles", titles);
        }
        return mav;
    }

    @PostMapping("/parse")
    public String parse(@RequestParam String keyword) throws IOException {
        GoogleSearchScraper scraper = new GoogleSearchScraper();
        titles = scraper.googleScraper(keyword);

        String filename = "results.csv";
        try (FileWriter writer = new FileWriter(filename)) {
            int lastIndex = titles.size() - 2;
            for (int i = 0; i < lastIndex; i++) {
                String title = titles.get(i);
                writer.append("\"");
                writer.append(title.replace("\"", "\"\""));
                writer.append("\"");
                writer.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/parse";
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadCSV() {
        String filename = "results.csv";
        try {
            File file = new File(filename);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    // Content-Disposition
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                    // Content-Type
                    .contentType(MediaType.parseMediaType("application/csv"))
                    // Content-Length
                    .contentLength(file.length()) //
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}