import time
from datetime import datetime
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import json
import sys


# 입력 월 - 현재 월 / 입력 일 계산 함수
def calculate_date(input_date):  # date : 'yyyy-mm-dd'
    input_year = int(input_date.split("-")[0])
    input_month = int(input_date.split("-")[1])
    input_day = str(int(input_date.split("-")[2]))  # 0n -> n 변환
    current_year = datetime.now().year
    current_month = datetime.now().month

    if input_year - current_year == 1:
        diff_month = 12
    else:
        diff_month = 0

    diff_month += input_month - current_month
    return diff_month, input_day


# 성인, 어린이, 객실을 얼만큼 증가, 감소해야하는지 계산하는 함수
def number_of_people_rooms(num_adults, num_rooms):
    default_adults = 2
    default_rooms = 1
    diff_adults = num_adults - default_adults
    diff_rooms = num_rooms - default_rooms
    return diff_adults, diff_rooms



def adjust_num(buttons, option, num):  # option : 0 -> 인원, 2 -> 객실
    increase_btn = buttons[option].find_element(By.CLASS_NAME, 'a83ed08757.c21c56c305.f38b6daa18.d691166b09.ab98298258.bb803d8689.f4d78af12a')
    if option == 0:
        decrease_btn = buttons[option].find_element(By.CLASS_NAME, 'a83ed08757.c21c56c305.f38b6daa18.d691166b09.ab98298258.bb803d8689.e91c91fa93')
        if num < 0:
            for i in range(abs(num)):
                decrease_btn.click()
        else:
            for i in range(num):
                increase_btn.click()
    else:
        for i in range(num):
            increase_btn.click()


chrome_options = Options()
chrome_options.add_experimental_option("detach", True)

browser = webdriver.Chrome(options=chrome_options)
browser.maximize_window()

# booking.com 접속
url = 'https://www.booking.com/'
browser.get(url)

# 로그인 혜택 안내 창 닫기
try:
    WebDriverWait(browser, 10).until(EC.presence_of_element_located((By.CLASS_NAME, 'a83ed08757.c21c56c305.f38b6daa18.d691166b09.ab98298258.f4552b6561')))
    browser.find_element(By.CLASS_NAME, 'a83ed08757.c21c56c305.f38b6daa18.d691166b09.ab98298258.f4552b6561').click()
except:
    print('로그인 혜택 안내 창 없음')

# 목적지 검색 후 클릭
dest = sys.argv[1]  # 목적지
browser.find_element(By.NAME, 'ss').send_keys(dest)  # 목적지 입력
WebDriverWait(browser, 5).until(EC.invisibility_of_element_located((By.XPATH, '//div[text() = "주변 인기 여행지"]')))
WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.CLASS_NAME, 'd7430561e2')))
browser.find_element(By.CLASS_NAME, 'd7430561e2').click()  # 가장 첫 번째 검색 결과 클릭

# browser.minimize_window()

# 날짜 선택
checkin_date = sys.argv[2]
checkout_date = sys.argv[3]
checkin_diff_month, checkin_day = calculate_date(checkin_date)

for i in range(checkin_diff_month):
    browser.find_element(By.CLASS_NAME, 'a83ed08757.c21c56c305.f38b6daa18.d691166b09.f671049264.f4552b6561.dc72a8413c.f073249358').click()

browser.find_element(By.XPATH, f'//*[@data-date = "{checkin_date}"]').click()
browser.find_element(By.XPATH, f'//*[@data-date = "{checkout_date}"]').click()

# 인원 및 객실 버튼 클릭
browser.find_element(By.XPATH, '//button[@data-testid = "occupancy-config"]').click()

num_adults, num_rooms = 1, 1
adjust_adults, adjust_rooms = number_of_people_rooms(num_adults, num_rooms)

adjust_buttons = browser.find_elements(By.XPATH, '//div[@class = "a7a72174b8"]')  # [0] -> 인원, [2] -> 객실
adjust_num(adjust_buttons, 0, adjust_adults)
adjust_num(adjust_buttons, 2, adjust_rooms)

# 검색 버튼 클릭
browser.find_element(By.XPATH, '//button[@type="submit"]').click()
WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.XPATH, '//h1[@aria-live="assertive"]')))

# 스크롤 다운
for i in range(20):
    browser.execute_script("window.scrollBy(0, 1000);")
    time.sleep(0.2)

# beautifulsoup 사용
html = browser.page_source
soup = BeautifulSoup(html, 'html.parser')

# 광고 제외 상위 5개
accommodation_dict = {}
accommodation_dict['accommodation'] = []
items = soup.find_all('div', {'data-testid': 'property-card'})
i = 0
for item in items:
    if i >= 5:
        break
    if item.find('span', class_='b30f8eb2d6'):
        print('광고')
        continue
    photo = item.find('img', {'data-testid': 'image'}).attrs['src']
    name = item.find('div', {'data-testid': 'title'}).text
    location = item.find('span', {'data-testid': 'address'}).text
    find_rate = item.find_all('div', class_='ac4a7896c7')
    rate = ''
    for string in find_rate:
        txt = string.text
        if len(txt) == 3:
            rate = txt
            break
    price = item.find('span', {'data-testid': 'price-and-discounted-price'}).text
    url = item.find('a', {'data-testid': 'title-link'}).attrs['href']
    accommodation_dict['accommodation'].append({
        'photo': photo,
        'name': name,
        'location': location,
        'rate': rate,
        'price': price,
        'url': url
    })
    i += 1

browser.quit()

json_file = 'src/main/resources/accommodation.json'
with open(json_file, 'w', encoding='utf-8') as f:
    json.dump(accommodation_dict, f, indent=4, ensure_ascii=False)
