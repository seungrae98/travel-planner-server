package com.haandy.travel_planner_server.domain.placeData.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haandy.travel_planner_server.domain.FileWatcher;
import com.haandy.travel_planner_server.domain.placeData.data.PlaceData;
import com.haandy.travel_planner_server.domain.placeData.dto.response.PlaceDataGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceDataService {

    final ObjectMapper objectMapper;

    public List<PlaceDataGetResponse> getPlaceDataList(
            String place
    ) {
        // 기존 json 파일 삭제
        FileWatcher fileWatcher = new FileWatcher("src/main/resources/place_data.json", 30000, 1000);
        fileWatcher.deleteFileIfExists();
        try {
            // 파이썬 실행 명령어 및 스크립트 경로 설정
            String pythonPath = "travel_planner_venv/bin/python"; // Python 실행 파일 경로
            String scriptPath = "python/place_data.py"; // Python 스크립트 경로

            // ProcessBuilder에 명령어와 파라미터 추가
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonPath, scriptPath, place
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
        boolean fileCreated = fileWatcher.waitForFile(); // json 파일이 생성될 때까지 대기
        if (fileCreated) {
            System.out.println("File created");
            // FileInputStream으로 JSON 파일을 읽음
            try (InputStream inputStream = new FileInputStream("src/main/resources/place_data.json")) {
                // JSON 파일에서 List<PlaceData>로 매핑
                List<PlaceData> placeDataList = objectMapper.readValue(inputStream,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, PlaceData.class));

                // placeDataList를 List<PlaceDataGetResponse>로 매핑
                return placeDataList.stream()
                        .map(PlaceDataGetResponse::from)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
                return List.of();
            }
        } else {
            System.out.println("파일이 생성되지 않았습니다.");
            return List.of();
        }
    }
}
