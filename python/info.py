from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import sys

chrome_options = Options()
chrome_options.add_experimental_option("detach", True)

browser = webdriver.Chrome(options=chrome_options)
browser.maximize_window()

# Google 지도 접속
url = 'https://www.google.co.kr/maps/'
browser.get(url)

location = sys.argv[1]  # 장소명 + 국가 + 도시

browser.find_element(By.TAG_NAME, 'input').send_keys(location)
WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.XPATH, '//div[@data-index="0"]')))
if browser.find_element(By.XPATH, '//*[@id="cell0x0"]/span[2]/span').text == 'Google 지도에 누락된 장소를 추가합니다.':
    print('존재하지 않는 장소')
    browser.quit()
    exit()
browser.find_element(By.XPATH, '//div[@data-index="0"]').click()
WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.XPATH, '//div[@class="rogA2c "]')))
WebDriverWait(browser, 5).until(EC.presence_of_element_located((By.XPATH, '//span[@class="a5H0ec"]')))

title_kor = browser.find_element(By.XPATH, '//h1[@class="DUwDvf lfPIob"]').text
try:
    title_eng = browser.find_element(By.XPATH, '//h2[@class="bwoZTb fontBodyMedium"]').text
    title = title_kor + '(' + title_eng + ')'
except:
    title = title_kor
print(title)

# beautifulsoup 사용
html = browser.page_source
soup = BeautifulSoup(html, 'html.parser')

try:
    address = soup.find('div', class_='rogA2c').find('div').text
except:
    address = ''
print(address)

try:
    opening_hours = soup.find('div', class_='t39EBf GUrTXd').attrs['aria-label'].split('.')[0]
except:
    opening_hours = ''

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
print(sorted_hours)

try:
    addmission = soup.find('div', class_='NKJo9')
    provider = addmission.find('span', class_='tQLcee Vxnq8').text
    price = addmission.find('div', class_='drwWxc').text
    url = addmission.find('a', class_='Sv1XLe fontTitleSmall').attrs['href']
    addmission_info = (provider, price, url)
except:
    addmission_info = ()
print(addmission_info)

try:
    web_site = soup.find('a', class_='CsEnBe').attrs['href']
except:
    web_site = ''
print(web_site)

browser.quit()
