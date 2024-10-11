from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import re
import json
import sys

chrome_options = Options()
chrome_options.add_experimental_option("detach", True)

browser = webdriver.Chrome(options=chrome_options)
browser.maximize_window()

# 네이버 항공권 접속
url = 'https://flight.naver.com/'
browser.get(url)

# 출발(button) 클릭
browser.find_element(By.XPATH, '//b[text() = "ICN"]').click()

# 출발지 검색 후 출발지 코드 저장
org = sys.argv[1]  # 출발 도시 or 공항 코드
browser.find_element(By.CLASS_NAME, 'autocomplete_input__qbYlb').send_keys(org)  # 출발지 입력
WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.CLASS_NAME, 'autocomplete_code__6DiFG')))  # 검색 결과가 뜰 때까지 대기
org_code = browser.find_element(By.CLASS_NAME, 'autocomplete_code__6DiFG').text  # 첫 번째 검색 결과 텍스트
browser.find_element(By.CLASS_NAME, 'autocomplete_search_item__8Wqp5').click()

# 도착(button) 클릭
browser.find_element(By.XPATH, '//b[text() = "도착"]').click()

# 도착지 검색 후 도착지 코드 저장
dest = sys.argv[2]  # 도착 도시 or 공항 코드
browser.find_element(By.CLASS_NAME, 'autocomplete_input__qbYlb').send_keys(dest)
WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.CLASS_NAME, 'autocomplete_code__6DiFG')))
dest_code = browser.find_element(By.CLASS_NAME, 'autocomplete_code__6DiFG').text

# 출발 및 도착 날짜
dpt_date = sys.argv[3]
rtn_date = sys.argv[4]

search_url = f'https://flight.naver.com/flights/international/{org_code}-{dest_code}-{dpt_date}/{dest_code}-{org_code}-{rtn_date}?adult=1&fareType=Y'
browser.get(search_url)

WebDriverWait(browser, 10).until(EC.presence_of_element_located((By.CLASS_NAME, 'searchBox_searchBox__r64Hx')))  # 로딩 화면 넘어갈 때까지 대기
WebDriverWait(browser, 60).until(EC.invisibility_of_element_located((By.CLASS_NAME, 'loadingProgress_loadingProgress__kgXNS')))  # 로딩창 사라질 때까지 대기

# 인기 항공편 버튼이 있다면 클릭
try:
    browser.find_element(By.CLASS_NAME, 'international_more__xkwrn').click()
except:
    print('인기 항공편 버튼이 없습니다.')

# beautifulsoup 사용
html = browser.page_source
soup = BeautifulSoup(html, 'html.parser')

# 항공편 상위 5개
flight_dict = {}
flight_dict['flights'] = []
flight_html = soup.find('div', class_='international_popular_flight_list__j4Xns')
if flight_html:  # 인기 항공편 목록이 있다면
    flights = flight_html.find_all('div', class_=re.compile('^concurrent_ConcurrentItemContainer__NDJda'))
else:
    flight_html = soup.find('div', class_='concurrent_ConcurrentList__pF_Kv')
    flights = flights = flight_html.find_all('div', class_=re.compile('^concurrent_ConcurrentItemContainer__NDJda'))

i = 0
for flight in flights:
    if i >= 5:
        break
    else:
        airline = flight.find('b', class_='airline_name__0Tw5w').text  # 항공사 정보
        airline_logo = flight.find('img', class_='airline_logo__S_bQ7').attrs['src']
        route_info = flight.find_all('div', class_='route_Route__HYsDn')  # 출발 / 도착 정보
        dpt_info = route_info[0]
        rtn_info = route_info[1]
        dpt_dpt_time = dpt_info.find_all(class_='route_time__xWu7a')[0].text  # 출발할 때 - 출발 시간
        dpt_dpt_apt = dpt_info.find_all(class_='route_code__S07WE')[0].text  # 출발할 때 - 출발 공항
        dpt_arv_time = dpt_info.find_all(class_='route_time__xWu7a')[1].text  # 출발할 때 - 도착 시간
        dpt_arv_apt = dpt_info.find_all(class_='route_code__S07WE')[1].text  # 출발할 때 - 도착 공항
        dpt_time = dpt_info.find(class_='route_details__F_ShG').text  # 출발할 때 - 소요 시간
        rtn_dpt_time = rtn_info.find_all(class_='route_time__xWu7a')[0].text  # 돌아올 때 - 출발 시간
        rtn_dpt_apt = rtn_info.find_all(class_='route_code__S07WE')[0].text  # 돌아올 때 - 출발 공항
        rtn_arv_time = rtn_info.find_all(class_='route_time__xWu7a')[1].text  # 돌아올 때 - 도착 시간
        rtn_arv_apt = rtn_info.find_all(class_='route_code__S07WE')[1].text  # 돌아올 때 - 도착 공항
        rtn_time = rtn_info.find(class_='route_details__F_ShG').text  # 돌아올 때 - 소요 시간
        price = flight.find(class_='item_num__aKbk4').text  # 가격
        flight_dict['flights'].append({
            'airline': airline,
            'airline_logo': airline_logo,
            'dpt_dpt_time': dpt_dpt_time,
            'dpt_dpt_apt': dpt_dpt_apt,
            'dpt_arv_time': dpt_arv_time,
            'dpt_arv_apt': dpt_arv_apt,
            'dpt_time': dpt_time,
            'rtn_dpt_time': rtn_dpt_time,
            'rtn_dpt_apt': rtn_dpt_apt,
            'rtn_arv_time': rtn_arv_time,
            'rtn_arv_apt': rtn_arv_apt,
            'rtn_time': rtn_time,
            'price': price,
            'url': search_url
        })
        i += 1

browser.quit()

json_file = 'src/main/resources/flights.json'
with open(json_file, 'w', encoding='utf-8') as f:
    json.dump(flight_dict, f, indent=4, ensure_ascii=False)
