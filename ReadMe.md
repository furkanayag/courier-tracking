### To run the project please walk through each step below

1. Make sure you have docker and docker compose on your env. -> \
   **docker --version** \
   **docker-compose --version**
2. Run **'docker-compose up --build'** when docker is up, and after it is completed, backend will be available at port 8080. \
   (Port 3306, 8080 should be free on your local env, before run command please free the related ports.)
3. (Optional) If you do not want to use docker, you can use local mysql server, java 21 and maven with local profile.
4. Run this curl to make sure project is up;
   **curl --location 'http://localhost:8080/api/health-check'**
5. Use curls below to get total distance and post a new location; \
   Post new location; \
   **curl --location 'http://localhost:8080/api/courier/location' \
   --header 'Content-Type: application/json' \
   --data '{
   "courierId":1,
   "lat":40.986106,
   "lon":29.1161293,
   "timestamp":"2025-06-29T15:48:13"
   }'** \
   Get total distance; \
   **curl --location 'http://localhost:8080/api/courier/total-distance?courierId=1'**
6. To connect database run the command below, password: **courier** \
   **docker exec -it courier-mysql mysql -u courier -p**
7. For any problem please contact me by **furkan_ayag@outlook.com** or **05445460103**
