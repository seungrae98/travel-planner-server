package com.haandy.travel_planner_server.domain.directionData.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haandy.travel_planner_server.domain.FileWatcher;
import com.haandy.travel_planner_server.domain.directionData.data.DirectionData;
import com.haandy.travel_planner_server.domain.directionData.dto.response.DirectionDataGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@RequiredArgsConstructor
public class DirectionDataService {

    final ObjectMapper objectMapper;

    public DirectionDataGetResponse getDirectionData(
            String origin,
            String destination
    ) {
        // 기존 json 파일 삭제
        FileWatcher fileWatcher = new FileWatcher("src/main/resources/direction.json", 30000, 1000);
        fileWatcher.deleteFileIfExists();
        try {
            // 파이썬 실행 명령어 및 스크립트 경로 설정
            String pythonPath = "travel_planner_venv/bin/python"; // Python 실행 파일 경로
            String scriptPath = "python/direction.py"; // Python 스크립트 경로

            // ProcessBuilder에 명령어와 파라미터 추가
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonPath, scriptPath, origin, destination
            );

            // 프로세스 실행
            Process process = processBuilder.start();

            // 실행 결과를 출력하기 위해 InputStream 사용
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
        boolean fileCreated = fileWatcher.waitForFile();
        if (fileCreated) {
            System.out.println("File created");
            try (InputStream inputStream = new FileInputStream("src/main/resources/direction.json")) {
                // JSON 데이터를 DirectionData 객체로 매핑 (단일 객체로 변경)
                DirectionData directionData = objectMapper.readValue(inputStream, DirectionData.class);

                return DirectionDataGetResponse.from(directionData);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("파일이 생성되지 않았습니다.");
            return null;
        }
    }
}