package co.kr.abacus.base.api.sample.service;

import co.kr.abacus.base.common.dao.CommonDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BqSampleService {

    private final CommonDao commonDao;

    public void getTaSampleData() {
        List<Object> dList = commonDao.selectList("BqTa.getSampleDataList");
        log.info(dList.toString());
    }
}
