CREATE DATABASE IF NOT EXISTS quiz_app;
USE quiz_app;


CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(20) DEFAULT 'STUDENT',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
);


CREATE TABLE `quizzes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `description` text,
  `duration_sec` int NOT NULL,
  `total_marks` int NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
);


CREATE TABLE `questions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quiz_id` int DEFAULT NULL,
  `text` text NOT NULL,
  `option_a` varchar(200) DEFAULT NULL,
  `option_b` varchar(200) DEFAULT NULL,
  `option_c` varchar(200) DEFAULT NULL,
  `option_d` varchar(200) DEFAULT NULL,
  `correct_opt` char(1) DEFAULT NULL,
  `marks` int DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `quiz_id` (`quiz_id`),
  CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`) ON DELETE CASCADE
);


INSERT INTO `questions` VALUES (1,1,'Which keyword is used to inherit a class in Java?','super','this','extends','implements','C',2),(2,1,'Which method is the entry point of a Java program?','start()','main()','run()','init()','B',2),(3,1,'Which of these is not a Java primitive type?','int','boolean','String','char','C',2),(4,2,'What is the correct file extension for Python files?','.java','.py','.pyth','.pt','B',5),(5,2,'Which keyword is used to define a function in Python?','func','def','function','lambda','B',5),(6,2,'Which collection is ordered and immutable in Python?','List','Dictionary','Tuple','Set','C',10);



CREATE TABLE `attempts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `quiz_id` int DEFAULT NULL,
  `score` int DEFAULT NULL,
  `attempt_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `finished_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `quiz_id` (`quiz_id`),
  CONSTRAINT `attempts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `attempts_ibfk_2` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`id`) ON DELETE CASCADE
);


CREATE TABLE `attempt_answers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `attempt_id` int DEFAULT NULL,
  `question_id` int DEFAULT NULL,
  `chosen_opt` char(1) DEFAULT NULL,
  `selected_opt` varchar(5) NOT NULL,
  `is_correct` tinyint(1) DEFAULT '0',
  `awarded_marks` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `attempt_id` (`attempt_id`),
  KEY `question_id` (`question_id`),
  CONSTRAINT `attempt_answers_ibfk_1` FOREIGN KEY (`attempt_id`) REFERENCES `attempts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `attempt_answers_ibfk_2` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`) ON DELETE CASCADE
);