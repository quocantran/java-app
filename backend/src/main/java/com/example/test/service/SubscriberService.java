package com.example.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.test.domain.Subscriber;
import com.example.test.domain.request.subscriber.CreateSubscriberDTO;
import com.example.test.domain.response.ResponseEmailJob;
import com.example.test.repository.JobRepository;
import com.example.test.repository.SkillRepository;
import com.example.test.repository.SubscriberRepository;
import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Job;
import com.example.test.domain.Skill;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final MailService mailService;
    private final JobRepository jobRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository,
            MailService mailService, JobRepository jobRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.mailService = mailService;
        this.jobRepository = jobRepository;

    }

    public CreateSubscriberDTO create(CreateSubscriberDTO entity) {
        List<Long> skills = entity.getSkills();
        Subscriber subscriber = this.subscriberRepository.findByEmail(entity.getEmail());

        List<Skill> skillsInDb = this.skillRepository.findByIdIn(skills);

        if (subscriber == null) {
            Subscriber res = new Subscriber();

            res.setSkills(skillsInDb);
            res.setEmail(entity.getEmail());
            res.setName(entity.getName());

            this.subscriberRepository.save(res);
        } else {
            subscriber.setSkills(skillsInDb);
            this.subscriberRepository.save(subscriber);
        }

        return entity;
    }

    public Boolean findByUserEmail(String email) {
        return subscriberRepository.existsByEmail(email);
    }

    public void sendEmailToSubscribers() {
        int page = 0;
        int size = 50;
        Pageable pageable = PageRequest.of(page, size);

        Page<Subscriber> subscriberPage;
        do {
            subscriberPage = this.subscriberRepository.findAll(pageable);
            List<Subscriber> subscribers = subscriberPage.getContent();

            for (Subscriber subscriber : subscribers) {
                List<Skill> skills = subscriber.getSkills();
                if (skills != null && !skills.isEmpty()) {
                    List<Job> jobs = this.jobRepository.findBySkillsIn(skills);
                    if (jobs != null && !jobs.isEmpty()) {
                        List<ResponseEmailJob> responseEmailJobs = jobs.stream()
                                .map(job -> job.convertJobToResponseEmailJob(job))
                                .collect(Collectors.toList());

                        this.mailService.sendEmailFromTemplateSync(subscriber.getEmail(),
                                "Cơ hội việc làm dành cho bạn!", "job.template", subscriber.getName(),
                                responseEmailJobs);
                    }
                }
            }

            page++;
            pageable = PageRequest.of(page, size);
        } while (subscriberPage.hasNext());
    }

    public Subscriber findByEmail(String email) throws BadRequestException {
        Subscriber subscriber = this.subscriberRepository.findByEmail(email);
        if (subscriber == null) {
            throw new BadRequestException("Subscriber not found");

        }
        return subscriber;
    }
}
