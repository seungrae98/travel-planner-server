from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import json
import sys

chrome_options = Options()
chrome_options.add_argument("headless")
chrome_options.add_experimental_option("detach", True)

browser = webdriver.Chrome(options=chrome_options)

# Google 지도 접속
url = 'https://www.google.co.kr/maps/'
browser.get(url)

location = sys.argv[1]  # 장소명 + 국가 + 도시

browser.find_element(By.TAG_NAME, 'input').send_keys(location)
WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.XPATH, '//div[@data-index="0"]')))  # 검색 결과 첫 번째 요소 기다리기
if browser.find_element(By.XPATH, '//*[@id="cell0x0"]/span[2]/span').text == 'Google 지도에 누락된 장소를 추가합니다.':
    print('존재하지 않는 장소')
    browser.quit()
    exit()
browser.find_element(By.XPATH, '//div[@data-index="0"]').click()

# 장소 이름
WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.XPATH, '//h1[@class="DUwDvf lfPIob"]')))  # 장소 이름 기다리기
name_kor = browser.find_element(By.XPATH, '//h1[@class="DUwDvf lfPIob"]').text
try:
    name_eng = browser.find_element(By.XPATH, '//h2[@class="bwoZTb fontBodyMedium"]').text
    name = name_kor + '(' + name_eng + ')'
except:
    name = name_kor

# beautifulsoup 사용
html = browser.page_source
soup = BeautifulSoup(html, 'html.parser')

try:
    address = soup.find('div', class_='rogA2c').find('div').text
except:
    address = '-'

try:
    opening_hours = soup.find('div', class_='t39EBf GUrTXd').attrs['aria-label'].split('.')[0]
except:
    opening_hours = '-'

# 영업 시간 문자열을 줄 단위로 분할
days_of_week = [
    "일요일",
    "월요일",
    "화요일",
    "수요일",
    "목요일",
    "금요일",
    "토요일"
]

# 영업 시간 문자열을 줄 단위로 분할
hours_list = [line.strip() for line in opening_hours.split(';') if line.strip()]

# 요일에 따라 영업 시간을 정렬 (요일이 포함된 경우도 처리)
def get_sort_key(hour_info):
    for day in days_of_week:
        if day in hour_info:  # 요일이 포함되어 있으면
            return days_of_week.index(day)
    return len(days_of_week)  # 기본값, 모든 요일에 해당하지 않으면 맨 뒤로

# 영업 시간 정렬
sorted_hours = sorted(hours_list, key=get_sort_key)
opening_hours = sorted_hours

# try:
#     admission_fee = soup.find('div', class_='drwWxc').text
# except:
#     admission_fee = '-'
try:
    admission = soup.find('div', class_='NKJo9')
    admission_provider = admission.find('span', class_='tQLcee Vxnq8').text
    admission_fee = admission.find('div', class_='drwWxc').text
    admission_url = admission.find('a', class_='Sv1XLe fontTitleSmall').attrs['href']
except:
    admission_provider = '-'
    admission_fee = '-'
    admission_url = '-'


try:
    web_site = soup.find('a', {'data-tooltip': '웹사이트 열기'}).attrs['href']
except:
    web_site = '-'

photo = []
# 사진이 있다면 클릭
try:
    WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.XPATH, '//*[@id="QA0Szd"]/div/div/div[1]/div[2]/div/div[1]/div/div/div[1]/div[1]/button')))
    browser.find_element(By.XPATH, '//*[@id="QA0Szd"]/div/div/div[1]/div[2]/div/div[1]/div/div/div[1]/div[1]/button').click()
    WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.XPATH, '//a[@href="#"]')))
    pictures = browser.find_elements(By.XPATH, '//div[@class="U39Pmb"]')

    i = 0
    for pic in pictures:
        if i >= 4:  # 최대 개수 설정
            break
        str = pic.get_attribute('style')
        url = str.split('"')[1]
        if len(url) <= 5:
            continue
        photo.append(url)
        i += 1
except:
    print('사진이 없습니다.')

if len(photo) < 4:
    cnt = 4 - len(photo)
    for i in range(cnt):
        photo.append('-')

browser.quit()

place_data = [
    {
        'name': name,
        'photo': photo,
        'address': address,
        'opening_hours': opening_hours,
        'admission_provider': admission_provider,
        'admission_fee': admission_fee,
        'admission_url': admission_url,
        'web_site': web_site,
    }
]

json_file = 'src/main/resources/place_data.json'
with open(json_file, 'w', encoding='utf-8') as f:
    json.dump(place_data, f, indent=4, ensure_ascii=False)
