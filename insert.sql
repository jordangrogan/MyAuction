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

-- 10 Bidlog

-- 5 Category

-- 5 BelongsTo

commit;