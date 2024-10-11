package com.haandy.travel_planner_server.domain.accommodationData.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haandy.travel_planner_server.domain.accommodationData.dto.response.AccommodationDataGetResponse;
import com.haandy.travel_planner_server.domain.accommodationData.data.AccommodationDataWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationDataService {

    final ObjectMapper objectMapper;

    public List<AccommodationDataGetResponse> getAccommodationDataList(
            String location,
            String start_date,
            String end_date
    ) {

        try {
            // 파이썬 실행 명령어 및 스크립트 경로 설정
            String pythonPath = "travel_planner_env/bin/python"; // Python 실행 파일 경로
            String scriptPath = "python/accommodation.py"; // Python 스크립트 경로

            // ProcessBuilder에 명령어와 파라미터 추가
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonPath, scriptPath, location, start_date, end_date
            );

            // 프로세스 실행
            Process process = processBuilder.start();

            //실행 결과를 출력하기 위해 InputStream 사용
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 에러 로그 확인
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // 프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode); // Exit Code: 0 -> 정상

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 그 이후 json 파일 읽기
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("accommodation.json");
            AccommodationDataWrapper flightDataWrapper = objectMapper.readValue(inputStream, AccommodationDataWrapper.class);

            return flightDataWrapper.getAccommodationDataList().stream()
                    .map(AccommodationDataGetResponse::from)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
