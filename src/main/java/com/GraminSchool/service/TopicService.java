package com.GraminSchool.service;

import com.GraminSchool.dto.TopicRequest;
import com.GraminSchool.dto.TopicResponseDTO;
import com.GraminSchool.entity.Chapter;
import com.GraminSchool.entity.Topic;
import com.GraminSchool.repo.ChapterRepository;
import com.GraminSchool.repo.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    public TopicResponseDTO saveTopic(TopicRequest request) {
        // ✅ Check for duplicate topic name (case-insensitive)
        topicRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
            throw new IllegalArgumentException("Topic with name '" + request.getName() + "' already exists.");
        });

        // ✅ Validate Chapter existence
        Chapter chapter = chapterRepository.findById(request.getChapter_id())
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with ID: " + request.getChapter_id()));

        // ✅ Build and save Topic
        Topic topic = Topic.builder()
                .name(request.getName())
                .image(request.getImage())
                .videoLink(request.getVideoLink())
                .totalVisit(0)
                .enable(request.isEnable())
                .other(request.getOther())
                .chapter(chapter)
                .build();
        Topic savedTopic;
        try {
            savedTopic = topicRepository.save(topic);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Duplicate topic name not allowed: " + request.getName());
        }
        // ✅ Build clean response DTO
        TopicResponseDTO responseDTO = TopicResponseDTO.builder()
                .id(savedTopic.getId())
                .name(savedTopic.getName())
                .image(savedTopic.getImage())
                .videoLink(savedTopic.getVideoLink())
                .enable(savedTopic.isEnable())
                .other(savedTopic.getOther())
                .chapter(
                        TopicResponseDTO.ChapterSummary.builder()
                                .id(chapter.getId())
                                .name(chapter.getName())
                                .unit(chapter.getUnit())
                                .subject(
                                        TopicResponseDTO.SubjectSummary.builder()
                                                .id(chapter.getSubject().getId())
                                                .name(chapter.getSubject().getName())
                                                .description(chapter.getSubject().getDescription())
                                                .course(
                                                        TopicResponseDTO.CourseSummary.builder()
                                                                .id(chapter.getSubject().getCourse().getId())
                                                                .courseName(chapter.getSubject().getCourse().getCourseName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();
        return responseDTO;
    }
}
