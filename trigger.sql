-- My Auction
-- Team 1
-- Triggers, Functions, Procedures
-- Jordan Grogan, John Wartonick, Wyatt Bobis

-- proc_putProduct
-- The procedure takes as inputs all the
--valid information that the user provides, infers other information (i.e., auction id and
--starting date), and records all information to the database.
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


-- constraint: the amount of the new bid on a product > the currently highest bid (i.e. "amount" attribute) on that product


-- trig_bidTimeUpdate


-- trig_updateHighBid


-- func_productCount(x, c)


-- func_bidCount(x, u)


-- func_buyingAmount(x, u)


-- trig_closeAuctions