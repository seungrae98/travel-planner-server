# -*coding: utf-8 -*
import io

import openai
import sys
import os

# OpenAI API 키 설정
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding = 'utf8')
openai.api_key = 'openai_api_key' # 수정 필요!
sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding = 'utf8')

def get_chatgpt_response(user_input):
    # ChatGPT에 요청을 보내고 응답을 받음
    response = openai.ChatCompletion.create(
        model="gpt-4o",  # 사용할 모델 설정 (gpt-3.5-turbo 또는 gpt-4)
        messages=[
            {
                "role": "user",
                "content": user_input
             },
        ],
        # response_format 지정하기
        response_format={"type":"json_object"}
    )
    return response.choices[0].message['content']

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python chatgpt.py 'your message'")
        sys.exit(1)

    user_input = sys.argv[1]
    response = get_chatgpt_response(user_input)
    print(response)