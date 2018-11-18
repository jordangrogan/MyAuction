-- My Auction
-- Team 1
-- Triggers, Functions, Procedures
-- Jordan Grogan, John Wartonick, Wyatt Bobis


-- proc_putProduct (NOT YET TESTED)
CREATE OR REPLACE PROCEDURE proc_putProduct (product_name in varchar2, product_description in varchar2, seller_id in number, category in varchar2, min_price in number, num_days in number) AS
auction_id number;
current_date date;
BEGIN
    SELECT MAX(auction_id)+1 INTO auction_id FROM product;
    SELECT c_date INTO current_date FROM oursysdate;
    
    INSERT INTO product (AUCTION_ID, NAME, DESCRIPTION, SELLER, START_DATE, MIN_PRICE, NUMBER_OF_DAYS, STATUS) VALUES 
        (auction_id, product_name, product_description, seller_id, current_date, min_price, num_days, 'under auction');
    INSERT INTO belongsto (AUCTION_ID, CATEGORY) VALUES (auction_id, category);
END;
/


-- trig_bidTimeUpdate (TESTED)
CREATE OR REPLACE TRIGGER trig_bidTimeUpdate
AFTER INSERT ON bidlog
DECLARE
    old_date DATE;
    new_date DATE;
BEGIN
    SELECT c_date INTO old_date from oursysdate;
    new_date := old_date + INTERVAL '5' SECOND;
    UPDATE OURSYSDATE SET c_date = new_date WHERE c_date = old_date;
end;
/


-- trig_updateHighBid (NOT YET TESTED)
CREATE OR REPLACE TRIGGER trig_updateHighBid
AFTER INSERT OR UPDATE ON bidlog
FOR EACH ROW
BEGIN
    UPDATE product SET amount=:NEW.amount WHERE auction_id=:NEW.auction_id;
END;
/


-- func_productCount(x, c) (NOT YET TESTED)
-- counts the number of products sold in the past x months for a specific categories c, where x and c are the function’s inputs.
CREATE OR REPLACE FUNCTION func_productCount (x in number, c in varchar2) return number
IS
    num_products number;
BEGIN
    SELECT COUNT(auction_id) INTO num_products FROM BELONGSTO WHERE category=c;
    RETURN (num_products);
END;
/


-- func_bidCount(x, u) (NOT YET TESTED)
-- counts the number of bids a specific user u has placed in the past x months, where x and u are the function’s inputs.
CREATE OR REPLACE FUNCTION func_bidCount (x in number, u in varchar2) return number
IS
    num_bids number;
BEGIN
    SELECT COUNT(auction_id) INTO num_bids FROM BIDLOG WHERE bidder=u;
    RETURN (num_bids);
END;
/


-- func_buyingAmount(x, u)


-- trig_closeAuctions