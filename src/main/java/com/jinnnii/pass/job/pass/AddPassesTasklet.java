package com.jinnnii.pass.job.pass;

import com.jinnnii.pass.domain.BulkPassEntity;
import com.jinnnii.pass.domain.PassEntity;
import com.jinnnii.pass.domain.UserEntity;
import com.jinnnii.pass.domain.UserGroupEntity;
import com.jinnnii.pass.domain.constant.BulkPassStatus;
import com.jinnnii.pass.domain.mapper.PassModelMapper;
import com.jinnnii.pass.repository.BulkPassRepository;
import com.jinnnii.pass.repository.PassRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AddPassesTasklet implements Tasklet {

    private final BulkPassRepository bulkPassRepository;
    private final PassRepository passRepository;

    public AddPassesTasklet(BulkPassRepository bulkPassRepository,
                            PassRepository passRepository) {
        this.bulkPassRepository = bulkPassRepository;
        this.passRepository = passRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        /**
         * todo : 이용권 시작 1일 전, 그룹 사용자 들에게 이용권 추가
         * 1. 발급 전 상태 이며, 시작 되기 하루 전인 벌크 이용권 조회
         * 2. 벌크 이용권의 사용자 그룹 조회
         * 3. 사용자 그룹의 사용자 리스트 조회
         * 4. 사용자에게 제공할 이용권 생성
         * 5. 벌크 이용권 상태를 발급 완료로 전환
         */

        final LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        List<BulkPassEntity> bulkPassList = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startedAt);

        int count= 0;
        for (BulkPassEntity bulkPass : bulkPassList){
            List<UserEntity> userList = bulkPass.getGroupEntity().getUserGroups()
                    .stream().map(UserGroupEntity::getUser).toList();
            count += addPasses(bulkPass, userList);

            bulkPass.setStatus(BulkPassStatus.COMPLETED);
        }
        log.info("이용권 {}건 추가 완료", count);
        return RepeatStatus.FINISHED;
    }

    private int addPasses(BulkPassEntity bulkPass, List<UserEntity> userList){
        List<PassEntity> passList = new ArrayList<>();

        for (UserEntity user : userList){
            PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPass);
            passEntity.setUserEntity(user);

            passList.add(passEntity);
        }
        return passRepository.saveAll(passList).size();
    }
}
