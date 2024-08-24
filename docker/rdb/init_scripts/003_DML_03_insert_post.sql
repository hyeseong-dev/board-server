USE board;

INSERT INTO post (name, isAdmin, contents, createTime, updateTime, views, categoryId, userId)
VALUES
-- 카테고리 1: 일반
('일상 공유', 0, '오늘 있었던 재미있는 일을 공유합니다.', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('주말 계획', 0, '이번 주말에 무엇을 할지 고민 중입니다.', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('새 취미 소개', 0, '최근에 시작한 새로운 취미를 소개합니다.', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('일상 속 소소한 행복', 0, '작은 것에서 행복을 찾는 방법', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('오늘의 감사일기', 0, '매일 감사한 일을 기록하는 습관', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('친구와의 추억', 0, '오랜만에 만난 친구와의 즐거운 시간', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('나의 버킷리스트', 0, '꼭 해보고 싶은 일들을 정리해봤어요', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('일상 속 작은 변화', 0, '조금씩 달라지는 나의 모습', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('오늘의 명언', 0, '오늘 하루를 살아가는데 도움이 된 명언', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('주변 사람들에게 감사 인사', 0, '고마운 사람들에게 감사 인사를 전해요', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('나만의 스트레스 해소법', 0, '일상 속 스트레스를 푸는 나만의 방법', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('오늘의 행운', 0, '오늘 있었던 작은 행운을 나눕니다', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('일주일 돌아보기', 0, '지난 일주일을 되돌아보는 시간', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('나의 소소한 성취', 0, '작지만 의미 있는 나의 성취들', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('오늘의 실수와 교훈', 0, '실수에서 배운 소중한 교훈들', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('나의 장단점', 0, '스스로를 돌아보는 시간', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('최근에 본 영화 후기', 0, '인상 깊게 본 영화에 대한 감상', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('오늘의 날씨와 기분', 0, '날씨가 기분에 미치는 영향', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('주변 사람들과의 소통', 0, '대화의 중요성에 대한 생각', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),
('나의 하루 루틴', 0, '매일 반복되는 나만의 일과', NOW(), NOW(), FLOOR(RAND()*100), 1, FLOOR(RAND()*10)+1),

-- 카테고리 2: 공지사항
('긴급 서버 점검 안내', 1, '내일 오전 2시부터 4시까지 서버 점검이 있을 예정입니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('새로운 기능 업데이트', 1, '이번 업데이트에서 추가된 새로운 기능을 소개합니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('커뮤니티 이용 규칙 변경', 1, '변경된 커뮤니티 이용 규칙을 확인해 주세요.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('여름 휴가 공지', 1, '8월 1일부터 5일까지 고객센터 휴무입니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('신규 이벤트 안내', 1, '다가오는 가을 맞이 특별 이벤트를 소개합니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('시스템 보안 강화 안내', 1, '보안 강화를 위한 비밀번호 변경 안내입니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('모바일 앱 출시', 1, '안드로이드와 iOS용 모바일 앱이 출시되었습니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('커뮤니티 매니저 모집', 1, '열정적인 커뮤니티 매니저를 모집합니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('사이트 개편 안내', 1, '더 나은 서비스를 위해 사이트가 개편됩니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('연말 이용자 설문조사', 1, '서비스 개선을 위한 이용자 설문에 참여해주세요.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('신규 광고 정책 안내', 1, '변경된 광고 정책을 안내드립니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('개인정보 처리방침 개정', 1, '개정된 개인정보 처리방침을 확인해 주세요.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('추석 연휴 고객센터 운영 안내', 1, '추석 연휴 기간 고객센터 운영 시간 안내입니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('冬季 스페셜 이벤트', 1, '겨울을 맞아 특별한 이벤트를 준비했습니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('연말 시스템 점검 일정', 1, '12월 31일 밤 11시부터 1월 1일 오전 1시까지 점검 예정입니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('새해 복 많이 받으세요', 1, '새해를 맞아 모든 회원분들께 감사 인사드립니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('2024년 서비스 개선 계획', 1, '올해 예정된 주요 서비스 개선 사항을 안내합니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('봄맞이 대청소 이벤트', 1, '오래된 게시물 정리 이벤트에 참여해주세요.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('긴급 보안 패치 적용 안내', 1, '발견된 보안 취약점 패치를 위해 잠시 서비스가 중단됩니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),
('이용자 백만 명 돌파 기념 이벤트', 1, '회원 여러분들께 감사의 마음을 전합니다.', NOW(), NOW(), FLOOR(RAND()*100), 2, FLOOR(RAND()*10)+1),

-- 카테고리 3: 질문
('자바 초보자 질문', 0, '자바에서 static 키워드의 정확한 의미가 궁금합니다.', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('Python 리스트 컴프리헨션', 0, '리스트 컴프리헨션의 성능상 이점이 궁금합니다.', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('JavaScript 비동기 처리', 0, 'Promise와 async/await의 차이점이 무엇인가요?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('SQL 조인 최적화', 0, '대용량 데이터에서 조인 성능을 높이는 방법은?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('React vs Vue', 0, '프로젝트에 어떤 프레임워크를 사용해야 할지 고민입니다.', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('Docker 네트워크 설정', 0, '컨테이너 간 통신 설정 방법이 궁금합니다.', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('머신러닝 모델 선택', 0, '분류 문제에 적합한 알고리즘 추천 부탁드립니다.', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('Git 브랜치 전략', 0, '대규모 팀에서 효과적인 Git 브랜치 전략은?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('NoSQL vs SQL', 0, '어떤 상황에서 NoSQL을 선택해야 하나요?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('CI/CD 파이프라인 구축', 0, 'Jenkins vs GitLab CI 어떤 것이 좋을까요?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('클라우드 서비스 선택', 0, 'AWS와 Azure 중 어떤 것을 사용해야 할까요?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('보안 취약점 점검', 0, '웹 애플리케이션의 보안 취약점을 점검하는 방법은?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('마이크로서비스 아키텍처', 0, '모놀리식에서 마이크로서비스로의 전환 팁?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('프론트엔드 성능 최적화', 0, 'React 애플리케이션의 렌더링 성능 개선 방법?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('데이터베이스 인덱싱', 0, '효과적인 인덱스 설계 전략이 궁금합니다.', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('코드 리팩토링', 0, '레거시 코드를 효과적으로 리팩토링하는 방법은?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1),
('API 설계 원칙', 0, 'RESTful API 설계시 주의해야 할 점들은?', NOW(), NOW(), FLOOR(RAND()*100), 3, FLOOR(RAND()*10)+1);