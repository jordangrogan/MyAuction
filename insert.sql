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
insert into Product (auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount) Values(2, 'shoes', 'runnin shoes', 'John', TO_DATE('2018-03-18 17:36:01', 'YYYY-MM-DD HH24:MI:SS'), 15, 3, 'Selling', 'Jeff', TO_DATE('2018-03-21 02:30:01', 'YYYY-MM-DD HH24:MI:SS'), 15);
insert into Product (auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount) Values(3, 'sink', 'the kitchen sink', 'Scott', TO_DATE('2018-04-18 17:40:01', 'YYYY-MM-DD HH24:MI:SS'), 12, 6, 'Sold', 'Mark', TO_DATE('2018-04-24 04:30:01', 'YYYY-MM-DD HH24:MI:SS'), 12);
insert into Product (auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount) Values(4, 'cup', 'very big', 'Evan', TO_DATE('2018-05-18 17:40:01', 'YYYY-MM-DD HH24:MI:SS'), 5, 2, 'Selling', 'Lexy', TO_DATE('2018-05-20 11:30:01', 'YYYY-MM-DD HH24:MI:SS'), 2);
insert into Product (auction_id, name, description, seller, start_date, min_price, number_of_days, status, buyer, sell_date, amount) Values(5, 'ball', 'a very bouncy ball', 'Nick', TO_DATE('2018-11-18 17:40:01', 'YYYY-MM-DD HH24:MI:SS'), 3, 7, 'Sold', 'Bob', TO_DATE('2018-11-25 01:30:01', 'YYYY-MM-DD HH24:MI:SS'), 3);

-- 10 Bidlog
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (1000, 1, 'Bob', TO_DATE('2018-11-23 12:29:01', 'YYYY-MM-DD HH24:MI:SS'), 35);
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (2000, 2, 'Jeff',TO_DATE('2018-03-21 02:29:01', 'YYYY-MM-DD HH24:MI:SS'), 15 );
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (3000, 3, 'Mark', TO_DATE('2018-04-24 04:29:01', 'YYYY-MM-DD HH24:MI:SS'), 12);
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (4000, 4, 'Lexy', TO_DATE('2018-05-20 11:29:01', 'YYYY-MM-DD HH24:MI:SS'), 2 );
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (5000, 5, 'Bob', TO_DATE('2018-11-25 01:29:01', 'YYYY-MM-DD HH24:MI:SS'), 3 );
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (1001, 1, 'John' , TO_DATE('2018-11-19 16:30:02', 'YYY-MM-DD HH24:MI:SS'), 20); 
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (1002, 1, 'Dave' , TO_DATE('2018-11-19 16:35:02', 'YYY-MM-DD HH24:MI:SS'), 22);
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (3001, 3, 'Lexy', TO_DATE('2018-04-20 04:50:05', 'YYYY-MM-DD HH24:MI:SS'), 4);
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (3002, 3, 'Bob', TO_DATE('2018-04-21 06:45:03', 'YYYY-MM-DD HH24:MI:SS'), 7);
insert into Bidlog (bidsn, auction_id, bidder, bid_time, amount) Values (5001, 5. 'Mike' TO_DATE('2018-11-23 05:35:01', 'YYYY-MM-DD HH24:MI:SS'), 2);
-- 5 Category
insert into Category (name, parent_category) Values ('Home', Null);
insert into Category (name, parent_category) Values ('Sports', Null);
insert into Category (name, parent_category) Values ('Balls', 'Sports');
insert into Category (name, parent_category) Values ('Kitchen', 'Home');
insert into Category (name, parent_category) Values ('Equipment', 'Sports');
-- 5 BelongsTo
insert into BelongsTo (auction_id, category) values (1, 'Equipment');
insert into BelongsTo (auction_id, category) values (2, 'Equipment');
insert into BelongsTo (auction_id, category) values (3, 'Kitchen');
insert into BelongsTo (auction_id, category) values (4, 'Kitchen');
insert into BelongsTo (auction_id, category) values (5, 'Equipment');

commit;