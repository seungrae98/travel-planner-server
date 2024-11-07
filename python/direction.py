from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import json
import sys

chrome_options = Options()
chrome_options.add_argument("headless")
chrome_options.add_experimental_option("detach", True)

browser = webdriver.Chrome(options=chrome_options)
browser.maximize_window()

# Google 지도 경로 접속
url = 'https://www.google.co.kr/maps/dir/'
browser.get(url)

browser.find_element(By.XPATH, '//img[@aria-label="대중교통"]').click()

org, dest = sys.argv[1], sys.argv[2]

# 출발지 입력
org_input = browser.find_element(By.XPATH, "//*[contains(@aria-label, '출발지')]")
org_input.send_keys(org)
org_input.send_keys(Keys.ENTER)

# 목적지 입력
dest_input = browser.find_element(By.XPATH, "//*[contains(@aria-label, '목적지')]")
dest_input.send_keys(dest)
dest_input.send_keys(Keys.ENTER)

# 경로 정보가 뜰 때까지 기다림
try:
    WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.CLASS_NAME, 'CMnFh')))
except:
    WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.CLASS_NAME, 'm6QErb.XiKgde')))

# beautifulsoup 사용
html = browser.page_source
soup = BeautifulSoup(html, 'html.parser')

direction_info = soup.find('div', class_='CMnFh')
direction = []
if direction_info is not None:  # 대중 교통이 있는 경우
    ways = direction_info.find_all('span', class_='mTOalf')
    for i in range(len(ways)):
        type = ways[i].find('img')
        if type is None:  # > 무시
            continue
        if type.attrs['alt'] == '지하철' or type.attrs['alt'] == '버스' or type.attrs['alt'] == '기차':
            way = type.attrs['alt'] + '(' + ways[i+1].find('span', class_='cukLmd').text + ')'
            direction.append(way)
        else:  # 도보
            direction.append(type.attrs['alt'])

else:  # 대중 교통이 없는 경우 (도보)
    try:
        way = soup.find('span', id='section-directions-trip-travel-mode-0').find('span').attrs['aria-label']
    except:
        way = '-'
    direction.append(way)

try:
    time = soup.find('div', class_='Fk3sm fontHeadlineSmall').text
except:
    time = '-'

direction = '-'.join(direction)
print(direction)
print(time)

browser.quit()

direction_data = [
    {
        'direction': direction,
        'time': time
    }
]

json_file = 'src/main/resources/direction.json'
with open(json_file, 'w', encoding='utf-8') as f:
    json.dump(direction_data, f, indent=4, ensure_ascii=False)
