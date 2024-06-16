use cinema_seoul;

DELIMITER //

DROP PROCEDURE IF EXISTS generate_seats; 

CREATE PROCEDURE generate_seats()
BEGIN    
    DECLARE screen_num_st INT DEFAULT 1;
    DECLARE screen_num_ed INT DEFAULT 4;
    DECLARE screen_num INT DEFAULT 0;
    DECLARE screen_name VARCHAR(100);
    DECLARE c_st INT DEFAULT 1;
    DECLARE c_ed INT DEFAULT 20;
    DECLARE r_st INT DEFAULT 1;
    DECLARE r_ed INT DEFAULT 11;
    DECLARE s_row CHAR(1);
    DECLARE total_seat INT;
    DECLARE seat_grade CHAR(1);
    DECLARE row_num INT;
    DECLARE col_num INT;
    
    WHILE screen_num_st <= screen_num_ed DO
        CASE screen_num_st
            WHEN 1 THEN SET screen_name := 'A';
            WHEN 2 THEN SET screen_name := 'B';
            WHEN 3 THEN SET screen_name := 'C';
            WHEN 4 THEN SET screen_name := 'D';
            ELSE SET screen_name := 'U';
        END CASE;

        SET total_seat := r_ed * c_ed;

        INSERT INTO screen (name, total_seat) VALUES (screen_name, total_seat);

        SET row_num := r_st;
        WHILE row_num <= r_ed DO
            CASE row_num
                WHEN 1 THEN SET s_row := 'A';
                WHEN 2 THEN SET s_row := 'B';
                WHEN 3 THEN SET s_row := 'C';
                WHEN 4 THEN SET s_row := 'D';
                WHEN 5 THEN SET s_row := 'E';
                WHEN 6 THEN SET s_row := 'F';
                WHEN 7 THEN SET s_row := 'G';
                WHEN 8 THEN SET s_row := 'H';
                WHEN 9 THEN SET s_row := 'I';
                WHEN 10 THEN SET s_row := 'J';
                WHEN 11 THEN SET s_row := 'K';
                ELSE SET s_row := 'U';
            END CASE;
            
            SET col_num := c_st;
            WHILE col_num <= c_ed DO
                IF row_num < 3 OR col_num < 3 OR col_num > 18 THEN
                    SET seat_grade := 'C';
                ELSEIF row_num BETWEEN 5 AND 9 AND col_num BETWEEN 5 AND 16 THEN
                    SET seat_grade := 'A';
                ELSE
                    SET seat_grade := 'B';
                END IF;
                
                INSERT INTO seat (s_col, s_row, seat_grade, screen_num) VALUES (col_num, s_row, seat_grade, screen_num_st);
                
                SET col_num := col_num + 1;
            END WHILE;
            
            SET row_num := row_num + 1;
        END WHILE;
        
        SET screen_num_st := screen_num_st + 1;
    END WHILE;

    COMMIT;
END //

DELIMITER ;

CALL generate_seats();
