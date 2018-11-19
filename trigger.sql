-- My Auction
-- Team 1
-- Triggers, Functions, Procedures
-- Jordan Grogan, John Wartonick, Wyatt Bobis


-- proc_putProduct
CREATE OR REPLACE PROCEDURE proc_putProduct (product_name in varchar2, product_description in varchar2, seller in varchar2, categories_csv in varchar2, min_price in number, num_days in number) AS
auction_id number;
current_date date;
BEGIN
    SELECT MAX(auction_id)+1 INTO auction_id FROM product;
    SELECT c_date INTO current_date FROM oursysdate;
    
    INSERT INTO product (AUCTION_ID, NAME, DESCRIPTION, SELLER, START_DATE, MIN_PRICE, NUMBER_OF_DAYS, STATUS) VALUES 
        (auction_id, product_name, product_description, seller, current_date, min_price, num_days, 'under auction');
    
    -- Take a comma separated value list of categories and put them in the belongsto table
    FOR i IN (SELECT trim(regexp_substr(categories_csv, '[^,]+', 1, LEVEL)) l FROM dual CONNECT BY LEVEL <= regexp_count(categories_csv, ',')+1)
        LOOP
            INSERT INTO belongsto (AUCTION_ID, CATEGORY) VALUES (auction_id, i.l);
        END LOOP;

END;
/
-- Test:
-- set transaction read write;
-- call proc_putProduct('testprod', 'testdescript', 'jog89', 'Equipment,Kitchen', 5, 7);
-- commit;


-- trig_bidTimeUpdate
CREATE OR REPLACE TRIGGER trig_bidTimeUpdate
AFTER INSERT ON bidlog
DECLARE
    old_date DATE;
    new_date DATE;
BEGIN
    SELECT c_date INTO old_date FROM oursysdate;
    new_date := old_date + INTERVAL '5' SECOND;
    UPDATE oursysdate SET c_date = new_date WHERE c_date = old_date;
end;
/
-- Test:
-- SELECT to_char(c_date,'HH24:MI:SS AM') FROM OURSYSDATE;
-- INSERT INTO BIDLOG (BIDSN) VALUES ('11');
-- SELECT to_char(c_date,'HH24:MI:SS AM') FROM OURSYSDATE;


-- trig_updateHighBid
CREATE OR REPLACE TRIGGER trig_updateHighBid
AFTER INSERT OR UPDATE ON bidlog
FOR EACH ROW
DECLARE
    current_sys_date DATE;
BEGIN
    SELECT c_date INTO current_sys_date from oursysdate;
    UPDATE product SET amount=:NEW.amount, buyer=:NEW.bidder, sell_date=current_sys_date WHERE auction_id=:NEW.auction_id;
END;
/
-- Test:
-- INSERT INTO bidlog (BIDSN, AUCTION_ID, BIDDER, BID_TIME, AMOUNT) VALUES ('11', '5', 'jog89', TO_DATE('2018-11-18 22:05:42', 'YYYY-MM-DD HH24:MI:SS'), '100');


-- func_productCount(x, c)
-- counts the number of products sold in the past x months for a specific categories c, where x and c are the function’s inputs.
CREATE OR REPLACE FUNCTION func_productCount (x in number, c in varchar2) return number IS
    current_sys_date date;
    x_months_ago date;
    num_products number;
BEGIN
    SELECT c_date INTO current_sys_date from oursysdate;
    x_months_ago := add_months(current_sys_date, x*-1);
    SELECT COUNT(BELONGSTO.auction_id) INTO num_products FROM BELONGSTO JOIN PRODUCT ON BELONGSTO.auction_id=PRODUCT.auction_id WHERE BELONGSTO.category=c AND PRODUCT.sell_date>x_months_ago;
    RETURN (num_products);
END;
/
-- Test:
-- SELECT func_productCount(5, 'Equipment') FROM dual;


-- func_bidCount(x, u)
-- counts the number of bids a specific user u has placed in the past x months, where x and u are the function’s inputs.
CREATE OR REPLACE FUNCTION func_bidCount (x in number, u in varchar2) return number
IS
    current_sys_date date;
    x_months_ago date;
    num_bids number;
BEGIN
    SELECT c_date INTO current_sys_date from oursysdate;
    x_months_ago := add_months(current_sys_date, x*-1);
    SELECT COUNT(auction_id) INTO num_bids FROM BIDLOG WHERE bidder=u AND bid_time>x_months_ago;
    RETURN (num_bids);
END;
/
-- Test:
-- SELECT func_bidCount(5, 'jog89') FROM dual;


-- func_buyingAmount(x, u)
-- calculates the total dollar amount a specific user u has spent in the past x months, where x and u are the function’s inputs.
CREATE OR REPLACE FUNCTION func_buyingAmount (x in number, u in varchar2) return number
IS
    total_amt number;
    current_sys_date date;
    x_months_ago date;
BEGIN
    SELECT c_date INTO current_sys_date from oursysdate;
    x_months_ago := add_months(current_sys_date, x*-1);
    SELECT SUM(amount) INTO total_amt FROM product WHERE buyer=u AND sell_date>x_months_ago AND status='sold';
    RETURN (total_amt);
END;
/
-- Test:
-- SELECT func_buyingAmount(12, 'jog89') FROM dual;


-- trig_closeAuctions
-- executes when the system time is updated. This trigger should check all the products in the system and close the auctions
-- (i.e., change the status to ‘close’ if it is ‘under auction’) of all products whose sell-date falls before the new system time.
CREATE OR REPLACE TRIGGER trig_closeAuctions
AFTER UPDATE ON oursysdate
DECLARE
    current_sys_date DATE;
BEGIN
    SELECT c_date INTO current_sys_date FROM oursysdate;
    UPDATE product SET status='closed' WHERE status='under auction' AND sell_date <= current_sys_date;
end;
/
-- Test:
-- SELECT auction_id,status FROM product;
-- UPDATE oursysdate SET c_date=TO_DATE('2018-11-17 17:38:39', 'YYYY-MM-DD HH24:MI:SS') WHERE c_date=TO_DATE('2018-11-15 17:38:39', 'YYYY-MM-DD HH24:MI:SS');
-- SELECT auction_id,status FROM product;