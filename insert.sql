-- My Auction
-- Team 1
-- Sample Data
-- Jordan Grogan, John Wartonick, Wyatt Bobis

-- 1 ourSysDATE
INSERT INTO ourSysDATE (C_DATE) VALUES (TO_DATE('2018-11-15 17:38:39', 'YYYY-MM-DD HH24:MI:SS'));

-- 3 Customer
INSERT INTO Customer (LOGIN, PASSWORD, NAME, ADDRESS, EMAIL) VALUES ('jog89', 'mypass', 'Jordan Grogan', '4200 Fifth Ave, Pgh, PA 15260', 'jog89@pitt.edu');
INSERT INTO Customer (LOGIN, PASSWORD, NAME, ADDRESS, EMAIL) VALUES ('jww36', 'mypass', 'John Wartonick', '4200 Fifth Ave, Pgh, PA 15260', 'jww36@pitt.edu');
INSERT INTO Customer (LOGIN, PASSWORD, NAME, ADDRESS, EMAIL) VALUES ('wjb39', 'mypass', 'Wyatt Bobis', '4200 Fifth Ave, Pgh, PA 15260', 'wjb39@pitt.edu');

-- 1 Administrator
INSERT INTO Administrator (LOGIN, PASSWORD, NAME, ADDRESS, EMAIL) VALUES ('admin', 'root', 'Administrator', '4200 Fifth Ave, Pgh, PA 15260', 'admin@pitt.edu');

-- 5 Product
insert into Product (auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount) Values(1, 'bike', 'a blue bike', 'Mike', TO_DATE('2018-11-18 17:40:01', 'YYYY-MM-DD HH24:MI:SS'), 40, 5, 'Sold', 'Bob', TO_DATE('2018-11-23 12:30:01', 'YYYY-MM-DD HH24:MI:SS'), 35);
insert into Product (auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount) Values(1, 'shoes', 'runnin shoes', 'John', TO_DATE('2018-03-18 17:36:01', 'YYYY-MM-DD HH24:MI:SS'), 15, 3, 'Selling', 'Jeff', TO_DATE('2018-03-21 02:30:01', 'YYYY-MM-DD HH24:MI:SS'), 15);
insert into Product (auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount) Values(1, 'sink', 'the kitchen sink', 'Scott', TO_DATE('2018-04-18 17:40:01', 'YYYY-MM-DD HH24:MI:SS'), 12, 6, 'Sold', 'Mark', TO_DATE('2018-04-24 04:30:01', 'YYYY-MM-DD HH24:MI:SS'), 12);
insert into Product (auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount) Values(1, 'cup', 'very big', 'Evan', TO_DATE('2018-05-18 17:40:01', 'YYYY-MM-DD HH24:MI:SS'), 5, 2, 'Selling', 'Lexy', TO_DATE('2018-05-20 11:30:01', 'YYYY-MM-DD HH24:MI:SS'), 2);
insert into Product (auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount) Values(1, 'ball', 'a very bouncy ball', 'Nick', TO_DATE('2018-11-18 17:40:01', 'YYYY-MM-DD HH24:MI:SS'), 3, 7, 'Sold', 'Bob', TO_DATE('2018-11-25 01:30:01', 'YYYY-MM-DD HH24:MI:SS'), 3);
-- 10 Bidlog
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values ();
-- 5 Category
insert into Category (name, parent_category) Values ();
insert into Category (name, parent_category) Values ();
insert into Category (name, parent_category) Values ();
insert into Category (name, parent_category) Values ();
insert into Category (name, parent_category) Values ();
-- 5 BelongsTo
insert into BelongsTo (auction_id, category);
insert into BelongsTo (auction_id, category);
insert into BelongsTo (auction_id, category);
insert into BelongsTo (auction_id, category);
insert into BelongsTo (auction_id, category);

commit;