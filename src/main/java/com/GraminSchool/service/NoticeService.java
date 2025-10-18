package com.GraminSchool.service;

import com.GraminSchool.entity.Notice;
import com.GraminSchool.repo.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    public Notice createNotice(String title, String message, String createdBy,String role){
        Notice notice = Notice.builder()
                .title(title)
                .message(message)
                .createdBy(createdBy)
                .role(role)
                .enabled(true)
                .build();
        return noticeRepository.save(notice);
    }

    public Notice updateNotice(Long id,String title,String message,boolean enabled){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        notice.setTitle(title);
        notice.setMessage(message);
        notice.setEnabled(enabled);
        return noticeRepository.save(notice);
    }
    public List<Notice> getAllNotices(){
        return noticeRepository.findAll();
    }
    public List<Notice> getActiveNotices() {
        return noticeRepository.findByEnabled(true);
    }
    public Notice getNoticeById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
    }

    public List<Notice> getNoticesByRole(String role) {
        return noticeRepository.findByRoleIgnoreCase(role);
    }

    public List<Notice> getNoticesByCreatedBy(String createdBy) {
        return noticeRepository.findByCreatedByIgnoreCase(createdBy);
    }


}
