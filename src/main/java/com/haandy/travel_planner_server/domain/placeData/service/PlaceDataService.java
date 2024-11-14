package com.haandy.travel_planner_server.domain.placeData.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haandy.travel_planner_server.domain.FileWatcher;
import com.haandy.travel_planner_server.domain.placeData.data.PlaceData;
import com.haandy.travel_planner_server.domain.placeData.dto.response.PlaceDataGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class PlaceDataService {

    final ObjectMapper objectMapper;

    public PlaceDataGetResponse getPlaceData(
            String place
    ) {
        try {
            // 파이썬 실행 설정
            String pythonPath = "travel_planner_venv/bin/python"; // Python 실행 파일 경로
            String scriptPath = "python/place_data.py"; // Python 스크립트 경로

            // ProcessBuilder 설정
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonPath, scriptPath, place
            );

            // 프로세스 시작
            Process process = processBuilder.start();

            // 표준 출력을 캡처 (Python 스크립트가 필요한 데이터를 출력한다고 가정)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder outputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line);
            }

            // 에러 로그 확인
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // 프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode); // Exit Code: 0 -> 정상 실행

            if (exitCode == 0) {
                // 캡처한 출력을 JSON으로 파싱
                String jsonData = outputBuilder.toString();
                PlaceData placeData = objectMapper.readValue(jsonData, PlaceData.class);

                return PlaceDataGetResponse.from(placeData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 문제가 발생한 경우 null 반환
        return null;
    }
}