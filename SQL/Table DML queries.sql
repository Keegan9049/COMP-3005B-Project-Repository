-- SQL DML SAMPLE QUERIES FOR MEMBER TABLE --
INSERT INTO Member (name, email, password, fitness_goal, height, weight) VALUES
('John', 'john@gmail.com', 'abc123', 'Gain Muscle Mass', 180, 140),
('Alex', 'alex@yahoo.com', '345xyz', 'Gain muscle', 175, 140);


-- SQL DML SAMPLE QUERIES FOR TRAINER TABLE --
INSERT INTO Trainer (name, email, password) VALUES
('Trainer Ellie', 'ellie@outlook.com', '678qwe'),
('Trainer Ryan', 'Ryan@gmail.com', '890iop');

-- SQL DML SAMPLE QUERIES FOR CLASS TABLE --
INSERT INTO Class (class_name, start_time, end_time) VALUES
('HIIT Cardio', '2024-09-15 @ 9am', '2024-09-15 @ 9am'),
('Shadow Boxing', '2024-09-17 @ 11am', '2024-09-17 @ 11:30am');


-- SQL DML SAMPLE QUERIES FOR PERSONAL TRAINING SESSION TABLE --
INSERT INTO PersonalTrainingSession (member_id, trainer_id, start_time, end_time) VALUES
(1, 1, '2024-09-20 @3pm', '2024-09-20 @4pm'),
(2, 2, '2024-09-21 @11am', '2024-09-21 @12pm');


-- SQL DML SAMPLE QUERIES FOR TRAINER AVAILABILITY TABLE --
INSERT INTO TrainerAvailability (trainer_id, start_time, end_time) VALUES
(1, '2024-09-20 @4pm', '2024-09-20 @5pm'),
(2, '2024-09-20 @6pm', '2024-09-20 @7pm');



-- SQL DML SAMPLE QUERIES FOR ROOM TABLE --
INSERT INTO Room (room_name) VALUES
('Room 1'),
('Room 2');

-- SQL DML SAMPLE QUERIES FOR ROOMBOOKING TABLE --
INSERT INTO RoomBooking (class_id, room_id, start_time, end_time) VALUES
(1, 1, '2024-09-20 4pm', '2024-09-20 @5pm'),
(2, 2, '2024-09-21 @12pm', '2024-09-21 @2pm');

-- SQL DML SAMPLE QUERIES FOR EQUIPMENT TABLE --
INSERT INTO Equipment (equipment_name, available) VALUES
('Smith Machine', true),
('Free Weights', true),
('Rowing Machine', false);

-- SQL DML SAMPLE QUERIES FOR EXERCISE ROUTINE TABLE --
INSERT INTO ExerciseRoutine (member_id, date, exercise, duration_minutes, calories_burned) VALUES
(1, '2024-09-5', 'Shadow Boxing', 30, 250),
(2, '2024-09-6', 'Weight Training', 60, 180);

-- SQL DML SAMPLE QUERIES FOR PAYMENT TABLE --
INSERT INTO Payment (member_id, amount, payment_method, payment_status) VALUES
(1, 100.00, 'Card', true),
(2, 100.00, 'Cash', true);
