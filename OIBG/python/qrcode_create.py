import os
import sys
import time

from bs4 import BeautifulSoup
from selenium import webdriver
import chromedriver_autoinstaller
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By


def qrcode_img(v1):
    
    # 크롬 옵션 설정
    chrome_options = webdriver.ChromeOptions()

    # 브라우저를 띄우지 않고 실행하는 옵션 추가
    chrome_options.add_argument('headless')

    # Chrome 드라이버 설치
    chromedriver_autoinstaller.install()

    # 웹 드라이버 생성
    driver = webdriver.Chrome(options=chrome_options)

    # QR코드생성 홈페이지 열기
    driver.get("http://mqr.kr/generate/text/")

    # 텍스트에 일련번호 넣기
    input = driver.find_element(By.XPATH, '//*[@id="form-text"]')
    input.send_keys(v1)

    # QR코드 생성버튼 클릭
    driver.find_element(By.XPATH, '//*[@id="form-submit"]').send_keys(Keys.ENTER)

    # 페이지가 로드될 때까지 잠시 기다리기
    time.sleep(1)

    # 이미지 가져오기
    img = driver.find_element(By.XPATH, '//*[@id="qr-code"]/img').get_attribute('src')

    # 웹페이지 닫기
    driver.quit()

    # QR코드 이미지 출력
    print(img)
    
def main(argv):
    qrcode_img(argv[1])


if __name__ == "__main__":
    main(sys.argv)
