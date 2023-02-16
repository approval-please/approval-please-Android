# 현명한 소비습관을 돕는 앱, 결재 부탁드립니다 - Android :snowman:
<img width="800" alt="image" src="https://user-images.githubusercontent.com/71416677/218636937-182b0ce6-a587-46f2-a313-aade817d5b09.png">           
                
**결재 부탁드립니다**는 구매 및 소비에 고민을 가지고 있는 사람들이 서로 조언하고 정보를 공유할 수 있는 **소비 조언 커뮤니티 서비스**입니다.                           
선택을 내리는 것이 어렵고 타인의 선택에 의지하는 경향이 늘어난 사람들에게, 물건 구매에 있어 최종 선택까지의 과정을 도와주는 서비스를 제공합니다.                
                        
- **구매를 망설이고 있는 모든 사람들**을 대상으로 합니다.
- **결정느림보**들의 결정을 도와주거나 조언을 해 줄 수 있고, 사용자 역시 결정느림보로서 고민을 공유할 수 있어요.
- 결정 느림보들은 **결재 서류**를 올리고, 결재를 받을 수 있어요.
- 최종 결정자는 바로 **나 자신**! 사용자들의 도움을 받아 의사결정능력을 길러 보세요!
- 결재서류 이외의 궁금증이나 하고 싶은 말이 있다면? **커뮤니티 기능**을 활용해 보세요.
- 결재서류를 많이 통과시킬수록 실적이 쌓여요! **승진 포인트**를 모아 직급을 높일 수 있어요.
                                            
                
## 📍 기능 소개
**🏠 로그인&홈**                      
                        
<img width="800" alt="image" src="https://user-images.githubusercontent.com/71416677/218637355-ef4cc2fd-64d4-4e53-9f05-1e6d8c465f21.png">         
                      
**📝 결재하기**                         
                        
<img width="800" alt="image" src="https://user-images.githubusercontent.com/71416677/218637446-eddb0af8-5271-4c1f-8b9e-6d8a95b48c0c.png">         
                            
**🗨️ 커뮤니티**                          
                        
<img width="800" alt="image" src="https://user-images.githubusercontent.com/71416677/218637504-d956c896-8524-4d83-9c13-650cd691a1d2.png">         

**💁 내 정보**                       
                        
<img width="800" alt="image" src="https://user-images.githubusercontent.com/71416677/218637565-fc592735-1c69-46e9-9195-4034106a376a.png">         
                                            
            
## 📍 프로젝트 기간
- 개발 : 2023.01 ~ 2023.02
- 리팩토링 : 2023.02 ~ 진행 중                         
                                        
                                        
## 📍 Developers
<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/Gseungmin"><img src="https://avatars.githubusercontent.com/u/87487149?v=4" width="200px;" alt=""/><br /><b>지승민</b></a><br /></td>
      <td align="center"><a href="https://github.com/aran-kim"><img src="https://avatars.githubusercontent.com/u/70698666?v=4" width="200px;" alt=""/><br /><b>김아란</b></a><br /></td>
      <td align="center"><a href="https://github.com/ChaeYubin"><img src="https://avatars.githubusercontent.com/u/63189595?v=4" width="200px;" alt=""/><br /><b>채유빈</b></a><br /></td>
      <td align="center"><a href="https://github.com/1ny0ung"><img src="https://avatars.githubusercontent.com/u/76527090?v=4" width="200px;" alt=""/><br /><b>최인영</b></a><br /></td>
     <tr/>
      <td align="center">Android Lead</td>
      <td align="center">Android</td>
      <td align="center">Android</td>
      <td align="center">Android</td>
    </tr>
  </tbody>
</table>  

## :herb: Git 협업 브랜치 전략
* 원본 Remote Repository를 깃허브에서 본인 계정으로 Fork
* Fork한 Remote Repository를 로컬 PC에 Clone
* Clone한 로컬 PC의 Repository에서 Branch 생성 후 작업 및 Commit
* Fork한 Repository에 PUSH 후 PR 생성
* Github Action CI를 통한 빌드 후 Merge        

## 📍 기술 스택
**Framework**
- Android Studio : Dolphin 2020.3.1
- kotlin : 1.8
- minSdk, targetSdk : 21, 32

**Infra**
- Github Actions
- AWS S3 

**Dependencies**
|Architecture|MVVM|
|:---:|:---:|
|Design Pattern|Repository Pattern, Adapter Pattern, Observer Pattern|
|Jetpack|ViewBinding, Datastore, Navigation, LiveData, ViewModel|
|Network|Retrofit2, Okhttp|
|DB|Room|
|Asynchronous Processing|Coroutine, ViewModelScope|
|Crawling|Jsoup|
|Image Cloud|AWS S3|
|Other Tool|Notion, Figma, Discord|
                                                    
## :moyai: 프로젝트 아키텍처
<img width="800" src="https://user-images.githubusercontent.com/87487149/219460010-334f5f80-21a0-4747-a4d9-16ca25d2f208.jpg">
