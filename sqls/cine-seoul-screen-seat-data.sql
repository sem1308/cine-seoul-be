DECLARE
	screen_num_st NUMBER := 1;
	screen_num_ed NUMBER := 4;
	screen_num NUMBER;
	screen_name VARCHAR2(100);
	c_st VARCHAR2(2) := '1';
    c_ed VARCHAR2(2) := '20';
	r_st NUMBER := 1;
    r_ed NUMBER := 11;
    s_row VARCHAR2(1);
	total_seat NUMBER;
    seat_grade VARCHAR2(1);
BEGIN
    FOR screen IN screen_num_st..screen_num_ed
    LOOP
        -- �󿵰� ����
        CASE screen
            WHEN 1 THEN screen_name := 'A';
            WHEN 2 THEN screen_name := 'B';
            WHEN 3 THEN screen_name := 'C';
            WHEN 4 THEN screen_name := 'D';
            ELSE screen_name := 'U';
        END CASE;
        total_seat := r_ed * c_ed;
        insert into screen(name,total_seat) values(screen_name,total_seat);        
        select screen_num into screen_num from screen where name=screen_name;        
        
        -- �¼� ����
        FOR row IN r_st..r_ed
        LOOP
            -- �� ����
            CASE row
                WHEN 1 THEN s_row := 'A';
                WHEN 2 THEN s_row := 'B';
                WHEN 3 THEN s_row := 'C';
                WHEN 4 THEN s_row := 'D';
                WHEN 5 THEN s_row := 'E';
                WHEN 6 THEN s_row := 'F';
                WHEN 7 THEN s_row := 'G';
                WHEN 8 THEN s_row := 'H';
                WHEN 9 THEN s_row := 'I';
                WHEN 10 THEN s_row := 'J';
                WHEN 11 THEN s_row := 'K';
                ELSE s_row := 'U';
            END CASE;
            FOR s_col IN c_st..c_ed
            LOOP
                -- �¼� ��� ����
                if row < 3 or s_col < '3' or s_col > '18' then
                    seat_grade := 'C';
                elsif row between 5 and 9 AND s_col between '5' and '16' then
                    seat_grade := 'A';
                else
                    seat_grade := 'B';
                end if;
                insert into seat(s_col,s_row,seat_grade,screen_num) values(s_col,s_row,seat_grade,screen_num);
--            DBMS_OUTPUT.PUT_LINE('�󿵰� '||screen_num || ' : �¼�' || s_row || s_col || '('|| seat_grade || ')'||' ����');
            END LOOP;	
        END LOOP;
    END LOOP;
END;

COMMIT;