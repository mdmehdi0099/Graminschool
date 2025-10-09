package com.GraminSchool.service;


import com.GraminSchool.dto.LectureRequest;
import com.GraminSchool.dto.LectureResponseDTO;
import com.GraminSchool.entity.Lecture;
import com.GraminSchool.entity.Topic;
import com.GraminSchool.repo.LectureRepository;
import com.GraminSchool.repo.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class LectureService {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private TopicRepository topicRepository;

    public LectureResponseDTO saveLecture(LectureRequest request) {
        // ✅ Validation
        if (lectureRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Lecture with name '" + request.getName() + "' already exists.");
        }

        Topic topic = topicRepository.findById(request.getTopic_id())
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with ID: " + request.getTopic_id()));

        // ✅ Build entity
        Lecture lecture = Lecture.builder()
                .name(request.getName())
                .image(request.getImage())
                .VideoLink(request.getVideoLink())
                .enable(request.isEnable())
                .MaxWatch(request.getMaxWatch())
                .Other(request.getOther())
                .TotalVisit(0)
                .topic(topic)
                .build();

        Lecture savedLecture;
        try {
            savedLecture = lectureRepository.save(lecture);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Duplicate lecture name not allowed: " + request.getName());
        }

        // ✅ Build response DTO
        LectureResponseDTO response = LectureResponseDTO.builder()
                .id(savedLecture.getId())
                .name(savedLecture.getName())
                .image(savedLecture.getImage())
                .videoLink(savedLecture.getVideoLink())
                .enable(savedLecture.isEnable())
                .maxWatch(savedLecture.getMaxWatch())
                .other(savedLecture.getOther())
                .topic(
                        LectureResponseDTO.TopicSummary.builder()
                                .id(topic.getId())
                                .name(topic.getName())
                                .chapter(
                                        LectureResponseDTO.ChapterSummary.builder()
                                                .id(topic.getChapter().getId())
                                                .name(topic.getChapter().getName())
                                                .unit(topic.getChapter().getUnit())
                                                .subject(
                                                        LectureResponseDTO.SubjectSummary.builder()
                                                                .id(topic.getChapter().getSubject().getId())
                                                                .name(topic.getChapter().getSubject().getName())
                                                                .description(topic.getChapter().getSubject().getDescription())
                                                                .course(
                                                                        LectureResponseDTO.CourseSummary.builder()
                                                                                .id(topic.getChapter().getSubject().getCourse().getId())
                                                                                .course_name(topic.getChapter().getSubject().getCourse().getCourse_name())
                                                                                .build()
                                                                )
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        return response;
    }
}

