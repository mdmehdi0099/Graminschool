package com.GraminSchool.service;

import com.GraminSchool.dto.ChapterRequest;
import com.GraminSchool.entity.Chapter;
import com.GraminSchool.entity.Subject;
import com.GraminSchool.repo.ChapterRepository;
import com.GraminSchool.repo.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    public Chapter saveChapter(ChapterRequest request){
        // ✅ Check for duplicate subject name (case-insensitive)
        chapterRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
            throw new IllegalArgumentException("Chapter with name '" + request.getName() + "' already exists.");
        });


        Subject subject=subjectRepository.findById(request.getSubject_id())
                .orElseThrow(()->new IllegalArgumentException("Subject not found with ID: "+request.getSubject_id()));

        //Build and set properties
        Chapter chapter=Chapter.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .image(request.getImage())
                .VideoLink(request.getVideoLink())
                .TotalVisit(0)
                .enable(request.isEnable())
                .Other(request.getOther())
                .subject(subject)
                .build();
        try{
        return chapterRepository.save(chapter);
        } catch (DataIntegrityViolationException e) {
            // fallback in case DB constraint triggers
            throw new IllegalArgumentException("Duplicate chapter name not allowed: " + request.getName());
        }
    }





}
