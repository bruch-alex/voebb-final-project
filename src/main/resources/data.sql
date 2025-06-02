--  Creator Roles
INSERT INTO creator_roles (creator_role_id, creator_role)
VALUES (1, 'Author'),
       (2, 'Co-Author'),
       (3, 'Editor'),
       (4, 'Director'),
       (5, 'Game Designer')
ON CONFLICT (creator_role_id) DO NOTHING;
SELECT SETVAL('creator_roles_creator_role_id_seq', (SELECT MAX(creator_role_id) FROM creator_roles));

-- Product types
INSERT INTO product_types (product_type_id, name, display_name, borrow_duration_days, main_creator_role_id, default_cover_url)
VALUES (1, 'book', 'Book', 28, 1, '/images/default-book.jpg'),
       (2, 'ebook', 'E-Book', 28, 1, '/images/default-book.jpg'),
       (3, 'dvd', 'DVD', 14, 4, '/images/default-dvd.jpg'), 
       (4, 'boardgame', 'Board Game', 7, 5, '/images/default-dvd.jpg')
ON CONFLICT (product_type_id) DO NOTHING;
SELECT SETVAL('product_types_product_type_id_seq', (SELECT MAX(product_type_id) FROM product_types));

-- Item status
INSERT INTO item_status (item_status_id, item_status_name)
VALUES (1, 'available'),
       (2, 'reserved'),
       (3, 'borrowed'),
       (4, 'damaged'),
       (5, 'lost')
ON CONFLICT (item_status_id) DO NOTHING;
SELECT SETVAL('item_status_item_status_id_seq', (SELECT MAX(item_status_id) FROM item_status));

-- Client Roles
INSERT INTO user_roles (role_id, role_name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_CLIENT')
ON CONFLICT (role_id) DO NOTHING;
SELECT SETVAL('user_roles_role_id_seq', (SELECT MAX(role_id) FROM user_roles));;

-- Languages  ─────────────────────────────────────────────────
INSERT INTO languages (language_id, language_name)
VALUES (1, 'English'),
       (2, 'German'),
       (3, 'Hindi'),
       (4, 'Chinese'),
       (5, 'Japanese'),
       (6, 'French'),
       (7, 'Spanish'),
       (8, 'Russian'),
       (9, 'Arabic'),
       (10, 'Korean')
ON CONFLICT (language_id) DO NOTHING;
SELECT SETVAL('languages_language_id_seq', (SELECT MAX(language_id) FROM languages));
-- DUMMY DATA BELLOW

-- Products with photos
INSERT INTO products (product_id, product_type_id, product_link_to_emedia, title, release_year, photo, description)
VALUES (1, 1, NULL, 'Harry Potter and the Philosopher''s Stone', '1997', 'photo_url_1', 'First book of Harry Potter series'),
       (2, 2, 'https://ebooks.voebb.de/hp1', 'Harry Potter and the Philosopher''s Stone', '1997', 'photo_url_1', 'First book of Harry Potter series'),
       (3, 1, NULL, 'Harry Potter and the Chamber of Secrets', '1998', 'photo_url_2', 'Second book in the series'),
       (4, 2, 'https://ebooks.voebb.de/hp2', 'Harry Potter and the Chamber of Secrets', '1998', 'photo_url_2', 'Second book in the series'),
       (5, 1, NULL, 'Harry Potter and the Prisoner of Azkaban', '1999', 'photo_url_3', 'Third book in the series'),
       (6, 2, 'https://ebooks.voebb.de/hp3', 'Harry Potter and the Goblet of Fire', '2000', 'photo_url_4', 'Fourth book in the series'),
       (7, 1, NULL, 'Harry Potter and the Order of the Phoenix', '2003', 'photo_url_5', 'Fifth book in the series'),
       (13, 4, NULL, 'Monopoly', '1935', 'photo_url_13', 'Holds the Guinness World Record for being played by the most people')
ON CONFLICT (product_id) DO NOTHING;

-- Products without photos
INSERT INTO products (product_id, product_type_id, product_link_to_emedia, title, release_year, description)
VALUES (8, 2, 'https://ebooks.voebb.de/hp4', 'Harry Potter and the Half-Blood Prince', '2005', 'Sixth book in the series'),
       (9, 1, NULL, 'Harry Potter and the Deathly Hallows', '2007', 'Final book in the series'),
       (10, 1, NULL, 'Harry Potter and the Cursed Child', '2016', 'Play based on Harry Potter universe'),
       (11, 1, NULL, 'Fantastic Beasts and Where to Find Them', '2001', 'A companion book to Harry Potter'),
       (12, 3, NULL, 'The Matrix', '1999', 'DVD format')
ON CONFLICT (product_id) DO NOTHING;
SELECT SETVAL('products_product_id_seq', (SELECT MAX(product_id) FROM products));

-- Book Details (for both physical books and e-books)
INSERT INTO book_details (product_id, book_isbn, book_edition, book_pages)
VALUES (1, '9780747532699', '1st Edition', 223),
       (2, '9780747532699', 'Digital Edition', 223),
       (3, '9780747538493', '1st Edition', 251),
       (4, '9780747538493', 'Digital Edition', 251),
       (5, '9780747542155', '1st Edition', 317),
       (6, '9780747542155', 'Digital Edition', 317),
       (7, '9780747546245', '1st Edition', 607),
       (8, '9780747546245', 'Digital Edition', 607),
       (9, '9780747595830', '1st Edition', 607),
       (10, '9781338216660', '1st Edition', 320),
       (11, '9781408880715', '1st Edition', 128)
ON CONFLICT DO NOTHING;

-- Creators
INSERT INTO creators (creator_id, creator_first_name, creator_last_name)
VALUES (1, 'J. K.', 'Rowling'),
       (2, 'John', 'Tiffany'),
       (3, 'Jack', 'Thorne'),
       (4, 'Elizabeth', 'Magie')
ON CONFLICT (creator_id) DO NOTHING;
SELECT SETVAL('creators_creator_id_seq', (SELECT MAX(creator_id) FROM creators));

-- Join table (same Creator ->  multiple  Roles; same Product -> multiple Creators)
INSERT INTO creator_product_relation (creator_id, product_id, creator_role_id)
VALUES
    -- Rowling on both books, different roles
    (1, 10, 1), -- AUTHOR on Cursed Child   (already there)
    (1, 11, 2), -- CO_AUTHOR on Fantastic Beasts

    -- Cursed Child still has two co-authors
    (2, 10, 2), -- Tiffany  CO_AUTHOR
    (3, 10, 2), -- Thorne   CO_AUTHOR

    (4, 13, 5)
ON CONFLICT(creator_id, product_id, creator_role_id) DO NOTHING;

--  Clients ─────────────────────────────────────────────────
-- Test password is 12345678
INSERT INTO custom_users (custom_user_id, first_name, last_name, email, phone_number, password, is_enabled, borrowed_products_count)
VALUES (1, 'Admin 1', 'Admin 1', 'admin1@example.com', '+49-135-1234567', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (2, 'Admin 2', 'Admin 2', 'admin2@example.com', '+49-135-1234568', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (3, 'Client 1', 'Client 1', 'client1@example.com', '+49-135-1234569', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (4, 'Client 2', 'Client 2', 'client2@example.com', '+49-135-1234561', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (5, 'Helly ', 'R.', 'helly@example.com', '+49-135-1234560', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (6, 'Mark', 'S', 'mark@example.com', '+49-135-1234562', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (7, 'Ronald', 'B.', 'ronald@example.com', '+49-135-1234565', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 5)
ON CONFLICT (custom_user_id) DO NOTHING;
SELECT SETVAL('custom_users_custom_user_id_seq', (SELECT MAX(custom_user_id) FROM custom_users));

-- Join table (same Client -> multiple Roles)
INSERT INTO users_roles_relation (custom_user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (5, 2),
       (6, 2),
       (7, 2)
ON CONFLICT (role_id, custom_user_id) DO NOTHING;

-- Countries ─────────────────────────────────────────────────
INSERT INTO countries (country_id, country_name)
VALUES (1, 'United Kingdom'),
       (2, 'United States'),
       (3, 'Canada'),
       (4, 'Germany'),
       (5, 'France'),
       (6, 'Italy'),
       (7, 'Spain'),
       (8, 'Australia'),
       (9, 'Japan'),
       (10, 'China'),
       (11, 'India'),
       (12, 'Brazil'),
       (13, 'Mexico'),
       (14, 'South Korea'),
       (15, 'Russia'),
       (16, 'Netherlands'),
       (17, 'Sweden'),
       (18, 'Norway'),
       (19, 'Switzerland'),
       (20, 'South Africa'),
       (21, 'Argentina'),
       (22, 'New Zealand')
ON CONFLICT (country_id) DO NOTHING;
SELECT SETVAL('countries_country_id_seq', (SELECT MAX(country_id) FROM countries));

-- Join table (product linked to countries)
INSERT INTO country_relation (product_id, country_id)
VALUES (1, 1), -- Philosopher's Stone published in the UK
       (1, 2), -- ... and in the US
       (10, 1), -- Cursed Child in the UK
       (11, 2);
-- Fantastic Beasts in the US

-- Join table (product linked to languages)
INSERT INTO language_relation (product_id, language_id)
VALUES (1, 1),
       (1, 2);

-- Libraries ─────────────────────────────────────────────────
INSERT INTO libraries (library_id, library_name, library_description, address_city, address_district, address_postcode,
                       address_street, address_house_nr, address_osm_link)
VALUES (1, 'Zentral- und Landesbibliothek Berlin (ZLB) - Amerika-Gedenkbibliothek', 'One of the main branches of the ZLB, located in Kreuzberg. Offers a vast collection of books and digital media.', 'Berlin', 'Friedrichshain-Kreuzberg', '10969', 'Blücherplatz', '1', 'https://www.openstreetmap.org/?mlat=52.5000&mlon=13.4300#map=16/52.5000/13.4300'),
       (2, 'Zentral- und Landesbibliothek Berlin (ZLB) - Berliner Stadtbibliothek', 'The central city library located in Mitte, featuring extensive archives and modern facilities.', 'Berlin', 'Mitte', '10178', 'Breite Straße', '30-36', 'https://www.openstreetmap.org/?mlat=52.5170&mlon=13.3900#map=16/52.5170/13.3900'),
       (3, 'Bezirkszentralbibliothek Friedrichshain-Kreuzberg', 'Main district library in Friedrichshain-Kreuzberg offering a wide range of literature and community programs.', 'Berlin', 'Friedrichshain-Kreuzberg', '10243', 'Frankfurter Allee', '149', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.4500#map=16/52.5100/13.4500'),
       (4, 'Bezirkszentralbibliothek Pankow', 'Central library for the Pankow district, providing books, multimedia, and educational workshops.', 'Berlin', 'Pankow', '13189', 'Wollankstraße', '17', 'https://www.openstreetmap.org/?mlat=52.5700&mlon=13.4200#map=16/52.5700/13.4200'),
       (5, 'Bezirkszentralbibliothek Neukölln', 'Neukölln district library with extensive resources, reading areas, and digital services.', 'Berlin', 'Neukölln', '12043', 'Karl-Marx-Straße', '66', 'https://www.openstreetmap.org/?mlat=52.4800&mlon=13.4300#map=16/52.4800/13.4300'),
       (6, 'Bezirkszentralbibliothek Charlottenburg-Wilmersdorf', 'Library serving the Charlottenburg-Wilmersdorf district with a diverse collection and cultural events.', 'Berlin', 'Charlottenburg-Wilmersdorf', '10585', 'Schloßstraße', '48', 'https://www.openstreetmap.org/?mlat=52.5000&mlon=13.2900#map=16/52.5000/13.2900'),
       (7, 'Bezirkszentralbibliothek Treptow-Köpenick', 'Main district library in Treptow-Köpenick, offering books, media, and community engagement activities.', 'Berlin', 'Treptow-Köpenick', '12435', 'Neue Krugallee', '12', 'https://www.openstreetmap.org/?mlat=52.4600&mlon=13.5600#map=16/52.4600/13.5600'),
       (8, 'Bezirkszentralbibliothek Tempelhof-Schöneberg', 'Library in Tempelhof-Schöneberg providing diverse literature and multimedia collections for all ages.', 'Berlin', 'Tempelhof-Schöneberg', '10829', 'Friedenau', '3', 'https://www.openstreetmap.org/?mlat=52.4800&mlon=13.3500#map=16/52.4800/13.3500'),
       (9, 'Bezirkszentralbibliothek Lichtenberg', 'Lichtenberg’s central library with a broad collection of books and digital media, plus local history resources.', 'Berlin', 'Lichtenberg', '10317', 'Möllendorffstraße', '10', 'https://www.openstreetmap.org/?mlat=52.5200&mlon=13.5100#map=16/52.5200/13.5100'),
       (10, 'Bezirkszentralbibliothek Marzahn-Hellersdorf', 'Main district library serving Marzahn-Hellersdorf with extensive educational and cultural resources.', 'Berlin', 'Marzahn-Hellersdorf', '12679', 'Allee der Kosmonauten', '23', 'https://www.openstreetmap.org/?mlat=52.5600&mlon=13.5900#map=16/52.5600/13.5900'),
       (11, 'Bezirkszentralbibliothek Reinickendorf', 'Reinickendorf district library offering a wide variety of books, events, and digital resources.', 'Berlin', 'Reinickendorf', '13407', 'Residenzstraße', '56', 'https://www.openstreetmap.org/?mlat=52.5800&mlon=13.3200#map=16/52.5800/13.3200'),
       (12, 'Bibliothek Mitte', 'Community library located in the heart of Mitte, Berlin, featuring cultural programs and extensive collections.', 'Berlin', 'Mitte', '10115', 'Invalidenstraße', '116', 'https://www.openstreetmap.org/?mlat=52.5300&mlon=13.3800#map=16/52.5300/13.3800'),
       (13, 'Bibliothek Prenzlauer Berg', 'Popular neighborhood library in Prenzlauer Berg offering a cozy reading environment and events.', 'Berlin', 'Pankow', '10405', 'Schönhauser Allee', '10', 'https://www.openstreetmap.org/?mlat=52.5400&mlon=13.4200#map=16/52.5400/13.4200'),
       (14, 'Bibliothek Köpenick', 'Local library in Köpenick providing access to books, media, and community services.', 'Berlin', 'Treptow-Köpenick', '12555', 'Alt-Köpenick', '30', 'https://www.openstreetmap.org/?mlat=52.4600&mlon=13.6100#map=16/52.4600/13.6100'),
       (15, 'Bibliothek Schöneberg', 'Library serving the Schöneberg area, focused on educational resources and community outreach.', 'Berlin', 'Tempelhof-Schöneberg', '10827', 'Grunewaldstraße', '5', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.3400#map=16/52.4700/13.3400'),
       (16, 'Bibliothek Wilmersdorf', 'Wilmersdorf neighborhood library with a rich collection of literature and multimedia resources.', 'Berlin', 'Charlottenburg-Wilmersdorf', '10715', 'Frankfurter Allee', '99', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.3200#map=16/52.5100/13.3200'),
       (17, 'Bibliothek Neukölln', 'Neighborhood library in Neukölln offering diverse collections and community-focused events.', 'Berlin', 'Neukölln', '12051', 'Hermannstraße', '125', 'https://www.openstreetmap.org/?mlat=52.4800&mlon=13.4300#map=16/52.4800/13.4300'),
       (18, 'Bibliothek Tempelhof', 'Tempelhof district library providing a variety of reading materials and digital access.', 'Berlin', 'Tempelhof-Schöneberg', '12101', 'Tempelhofer Damm', '14', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.4000#map=16/52.4700/13.4000'),
       (19, 'Bibliothek Lichtenberg', 'Library in Lichtenberg district focused on providing local history materials and general collections.', 'Berlin', 'Lichtenberg', '10365', 'Frankfurter Allee', '232', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.5200#map=16/52.5100/13.5200'),
       (20, 'Bibliothek Friedrichshain', 'Friedrichshain library offering extensive resources for education and entertainment.', 'Berlin', 'Friedrichshain-Kreuzberg', '10247', 'Boxhagener Straße', '60', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.4600#map=16/52.5100/13.4600'),
       (21, 'Bibliothek Reinickendorf','Reinickendorf library focused on children’s books and community programs.','Berlin', 'Reinickendorf', '13403', 'Oranienburger Straße', '20', 'https://www.openstreetmap.org/?mlat=52.6000&mlon=13.3200#map=16/52.6000/13.3200'),
       (22, 'Bibliothek Marzahn', 'Marzahn district library providing educational programs and diverse media.', 'Berlin', 'Marzahn-Hellersdorf', '12679', 'Alice-Salomon-Platz', '4', 'https://www.openstreetmap.org/?mlat=52.5600&mlon=13.5800#map=16/52.5600/13.5800'),
       (23, 'Bibliothek Charlottenburg', 'Library in Charlottenburg district offering a wide range of literature and digital collections.', 'Berlin', 'Charlottenburg-Wilmersdorf', '10585', 'Schloßstraße', '1', 'https://www.openstreetmap.org/?mlat=52.5000&mlon=13.2900#map=16/52.5000/13.2900'),
       (24, 'Bibliothek Köpenick Nord', 'Northern Köpenick branch with a focus on local culture and community engagement.', 'Berlin', 'Treptow-Köpenick', '12555', 'Kietzer Straße', '23', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.6000#map=16/52.4700/13.6000'),
       (25, 'Bibliothek Pankow Nord', 'Northern Pankow branch focused on family programs and diverse media.', 'Berlin', 'Pankow', '13187', 'Schönhauser Allee', '175', 'https://www.openstreetmap.org/?mlat=52.5500&mlon=13.4300#map=16/52.5500/13.4300'),
       (26, 'Bibliothek Friedrichshain Nord', 'Northern Friedrichshain branch providing local literature and multimedia resources.', 'Berlin', 'Friedrichshain-Kreuzberg', '10249', 'Landsberger Allee', '15', 'https://www.openstreetmap.org/?mlat=52.5200&mlon=13.4600#map=16/52.5200/13.4600'),
       (27, 'Bibliothek Mitte Süd', 'South Mitte branch offering books, media, and educational activities.', 'Berlin', 'Mitte', '10179', 'Leipziger Straße', '57', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.3900#map=16/52.5100/13.3900'),
       (28, 'Bibliothek Schöneberg Süd', 'South Schöneberg branch with a wide range of books and community events.', 'Berlin', 'Tempelhof-Schöneberg', '10827', 'Dominicusstraße', '4', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.3500#map=16/52.4700/13.3500'),
       (29, 'Bibliothek Neukölln Nord', 'Northern Neukölln branch offering community programs and a broad selection of books.', 'Berlin', 'Neukölln', '12053', 'Karl-Marx-Straße', '150', 'https://www.openstreetmap.org/?mlat=52.4900&mlon=13.4300#map=16/52.4900/13.4300'),
       (30, 'Bibliothek Treptow Nord', 'Northern Treptow library focusing on digital media and community engagement.', 'Berlin', 'Treptow-Köpenick', '12439', 'Neue Krugallee', '40', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.5600#map=16/52.4700/13.5600'),
       (31, 'Bibliothek Tempelhof Nord', 'Northern Tempelhof branch with a comprehensive selection of literature and media.', 'Berlin', 'Tempelhof-Schöneberg', '12101', 'Holzmarktstraße', '7', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.3900#map=16/52.4700/13.3900'),
       (32, 'Bibliothek Lichtenberg Nord', 'Northern Lichtenberg library offering extensive children’s books and study spaces.', 'Berlin', 'Lichtenberg', '10367', 'Roedernallee', '6', 'https://www.openstreetmap.org/?mlat=52.5200&mlon=13.5200#map=16/52.5200/13.5200'),
       (33, 'Bibliothek Marzahn Nord', 'Northern Marzahn branch focused on community events and educational resources.', 'Berlin', 'Marzahn-Hellersdorf', '12685', 'Hellersdorfer Straße', '12', 'https://www.openstreetmap.org/?mlat=52.5700&mlon=13.5900#map=16/52.5700/13.5900'),
       (34, 'Bibliothek Charlottenburg Nord', 'Northern Charlottenburg library with a strong focus on digital access and modern literature.', 'Berlin', 'Charlottenburg-Wilmersdorf', '14059', 'Wilhelmstraße', '50', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.2900#map=16/52.5100/13.2900'),
       (35, 'Bibliothek Wilmersdorf Nord', 'Northern Wilmersdorf branch offering a variety of collections and community programs.', 'Berlin', 'Charlottenburg-Wilmersdorf', '10715', 'Bismarckstraße', '60', 'https://www.openstreetmap.org/?mlat=52.5200&mlon=13.3100#map=16/52.5200/13.3100'),
       (36, 'Bibliothek Friedenau Nord', 'Northern Friedenau library with a wide selection of books and local history materials.', 'Berlin', 'Tempelhof-Schöneberg', '12159', 'Bundesallee', '110', 'https://www.openstreetmap.org/?mlat=52.4600&mlon=13.3500#map=16/52.4600/13.3500'),
       (37, 'Bibliothek Schöneberg West', 'Western Schöneberg branch offering diverse educational materials and multimedia.', 'Berlin', 'Tempelhof-Schöneberg', '10827', 'Dominicusstraße', '6', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.3500#map=16/52.4700/13.3500'),
       (38, 'Bibliothek Mitte West', 'Western Mitte branch focused on historical collections and digital resources.', 'Berlin', 'Mitte', '10179', 'Leipziger Platz', '10', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.3900#map=16/52.5100/13.3900'),
       (39, 'Bibliothek Friedrichshain West', 'Western Friedrichshain branch with extensive literary and digital offerings.', 'Berlin', 'Friedrichshain-Kreuzberg', '10249', 'Boxhagener Platz', '5', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.4600#map=16/52.5100/13.4600'),
       (40, 'Bibliothek Pankow West', 'Western Pankow library offering community-focused events and varied collections.', 'Berlin', 'Pankow', '13187', 'Schönhauser Allee', '180', 'https://www.openstreetmap.org/?mlat=52.5500&mlon=13.4300#map=16/52.5500/13.4300'),
       (41, 'Bibliothek Neukölln West', 'Western Neukölln branch with focus on youth programs and digital media.', 'Berlin', 'Neukölln', '12053', 'Karl-Marx-Straße', '160', 'https://www.openstreetmap.org/?mlat=52.4900&mlon=13.4300#map=16/52.4900/13.4300'),
       (42, 'Bibliothek Reinickendorf West','Western Reinickendorf library offering quiet study spaces and community resources.','Berlin', 'Reinickendorf', '13409', 'Oranienburger Straße', '40', 'https://www.openstreetmap.org/?mlat=52.6000&mlon=13.3200#map=16/52.6000/13.3200'),
       (43, 'Bibliothek Hohenschönhausen', 'Local library in Hohenschönhausen with a variety of books and digital media.', 'Berlin', 'Lichtenberg', '13057', 'Wartenberger Straße', '70', 'https://www.openstreetmap.org/?mlat=52.5700&mlon=13.5300#map=16/52.5700/13.5300'),
       (44, 'Bibliothek Friedrichshagen', 'Local library in Friedrichshagen, offering a peaceful reading environment and community programs.', 'Berlin', 'Treptow-Köpenick', '12587', 'Rahnsdorfer Straße', '3', 'https://www.openstreetmap.org/?mlat=52.4800&mlon=13.6300#map=16/52.4800/13.6300'),
       (45, 'Bibliothek Alt-Treptow', 'Neighborhood library in Alt-Treptow with a broad selection of literature and digital offerings.', 'Berlin', 'Friedrichshain-Kreuzberg', '12435', 'Plesser Straße', '14', 'https://www.openstreetmap.org/?mlat=52.4600&mlon=13.5600#map=16/52.4600/13.5600'),
       (46, 'Bibliothek Wedding', 'Wedding district library providing diverse collections and community programs.', 'Berlin', 'Mitte', '13347', 'Rehberge', '20', 'https://www.openstreetmap.org/?mlat=52.5500&mlon=13.3500#map=16/52.5500/13.3500'),
       (47, 'Bibliothek Spandau', 'Spandau district library with extensive resources and cultural events.', 'Berlin', 'Spandau', '13597', 'Carl-Schurz-Straße', '30', 'https://www.openstreetmap.org/?mlat=52.5400&mlon=13.2000#map=16/52.5400/13.2000'),
       (48, 'Bibliothek Reinickendorf Nord', 'Northern Reinickendorf branch focusing on educational resources and community services.', 'Berlin', 'Reinickendorf', '13405', 'Residenzstraße', '75', 'https://www.openstreetmap.org/?mlat=52.5800&mlon=13.3200#map=16/52.5800/13.3200'),
       (49, 'Bibliothek Rudow', 'Rudow neighborhood library offering a comprehensive collection and community events.', 'Berlin', 'Neukölln', '12357', 'Johannisthaler Chaussee', '200', 'https://www.openstreetmap.org/?mlat=52.4300&mlon=13.4400#map=16/52.4300/13.4400'),
       (50, 'Bibliothek Lankwitz', 'Lankwitz branch with a diverse selection of books and digital media.', 'Berlin', 'Steglitz-Zehlendorf', '12247', 'Lankwitzer Straße', '14', 'https://www.openstreetmap.org/?mlat=52.4500&mlon=13.3200#map=16/52.4500/13.3200'),
       (51, 'Bibliothek Zehlendorf Süd', 'Southern Zehlendorf branch offering a peaceful atmosphere with extensive collections in various media formats.', 'Berlin', 'Steglitz-Zehlendorf', '14163', 'Clayallee', '200', 'https://www.openstreetmap.org/?mlat=52.4400&mlon=13.2600#map=16/52.4400/13.2600'),
       (52, 'Bibliothek Steglitz West', 'West Steglitz library providing a wide range of literature, digital resources, and community programs.', 'Berlin', 'Steglitz-Zehlendorf', '12165', 'Schloßstraße', '55', 'https://www.openstreetmap.org/?mlat=52.4500&mlon=13.2800#map=16/52.4500/13.2800'),
       (53, 'Bibliothek Tempelhof West', 'Western Tempelhof branch known for its rich collection of fiction and non-fiction books.', 'Berlin', 'Tempelhof-Schöneberg', '12101', 'Friedrich-Wilhelm-Straße', '15', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.3700#map=16/52.4700/13.3700'),
       (54, 'Bibliothek Treptow West', 'Western Treptow library focusing on local history and educational workshops.', 'Berlin', 'Treptow-Köpenick', '12435', 'Alt-Treptow', '7', 'https://www.openstreetmap.org/?mlat=52.4600&mlon=13.5500#map=16/52.4600/13.5500'),
       (55, 'Bibliothek Neukölln Süd', 'Southern Neukölln branch offering a broad selection of books and multimedia for all ages.', 'Berlin', 'Neukölln', '12057', 'Rudower Straße', '50', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.4500#map=16/52.4700/13.4500'),
       (56, 'Bibliothek Kreuzberg Süd', 'Southern Kreuzberg library with a vibrant cultural program and multilingual collections.','Berlin', 'Friedrichshain-Kreuzberg', '10997', 'Adalbertstraße', '20', 'https://www.openstreetmap.org/?mlat=52.5000&mlon=13.4200#map=16/52.5000/13.4200'),
       (57, 'Bibliothek Lichtenberg Süd ','Southern Lichtenberg branch providing community services and a large children’s section. ','Berlin', 'Lichtenberg', '10365', 'Frankfurter Allee', '123', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.5200#map=16/52.5100/13.5200' ),
       (58, 'Bibliothek Hellersdorf Süd ','Southern Hellersdorf library offering modern media and quiet reading rooms.','Berlin', 'Marzahn-Hellersdorf', '12629', 'Hellersdorfer Straße', '44', 'https://www.openstreetmap.org/?mlat=52.5600&mlon=13.6200#map=16/52.5600/13.6200' ),
       (59, 'Bibliothek Reinickendorf Süd','Southern Reinickendorf branch focused on youth programs and digital collections.','Berlin', 'Reinickendorf', '13409', 'Residenzstraße', '12', 'https://www.openstreetmap.org/?mlat=52.5900&mlon=13.3100#map=16/52.5900/13.3100' ),
       (60, 'Bibliothek Spandau Süd','Southern Spandau library offering a comprehensive selection of media and cultural events.','Berlin', 'Spandau', '13585', 'Altstadt Spandau', '38', 'https://www.openstreetmap.org/?mlat=52.5400&mlon=13.1900#map=16/52.5400/13.1900' ),
       (61, 'Bibliothek Pankow Süd','Southern Pankow branch with a focus on educational support and multimedia collections.','Berlin', 'Pankow', '13187', 'Fröbelstraße', '8', 'https://www.openstreetmap.org/?mlat=52.5400&mlon=13.4200#map=16/52.5400/13.4200' ),
       (62, 'Bibliothek Wedding Süd','Southern Wedding library providing various learning materials and community programs.','Berlin', 'Mitte', '13347', 'Seestraße', '45', 'https://www.openstreetmap.org/?mlat=52.5500&mlon=13.3500#map=16/52.5500/13.3500' ),
       (63, 'Bibliothek Friedrichshain Süd','Southern Friedrichshain branch featuring modern media and community workshops.','Berlin', 'Friedrichshain-Kreuzberg', '10245', 'Samariterstraße', '12', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.4600#map=16/52.5100/13.4600' ),
       (64, 'Bibliothek Schöneberg Ost','Eastern Schöneberg branch with a focus on local history and cultural events.','Berlin', 'Tempelhof-Schöneberg', '10827', 'Akazienstraße', '19', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.3500#map=16/52.4700/13.3500' ),
       (65, 'Bibliothek Charlottenburg Ost','Eastern Charlottenburg library offering a wide range of literature and digital media.','Berlin', 'Charlottenburg-Wilmersdorf', '10585', 'Kantstraße', '50', 'https://www.openstreetmap.org/?mlat=52.5000&mlon=13.3000#map=16/52.5000/13.3000' ),
       (66, 'Bibliothek Marzahn Ost','Eastern Marzahn branch focused on family-friendly services and community engagement.','Berlin', 'Marzahn-Hellersdorf', '12679', 'Allee der Kosmonauten', '15', 'https://www.openstreetmap.org/?mlat=52.5700&mlon=13.6100#map=16/52.5700/13.6100' ),
       (67, 'Bibliothek Neukölln Ost','Eastern Neukölln library offering multilingual collections and cultural programs.','Berlin', 'Neukölln', '12051', 'Karl-Marx-Straße', '105', 'https://www.openstreetmap.org/?mlat=52.4900&mlon=13.4300#map=16/52.4900/13.4300' ),
       (68, 'Bibliothek Treptow Ost','Eastern Treptow library providing access to educational resources and digital media.','Berlin', 'Treptow-Köpenick', '12435', 'Neue Krugallee', '70', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.5600#map=16/52.4700/13.5600' ),
       (69, 'Bibliothek Tempelhof Ost','Eastern Tempelhof branch with a broad collection of literature and community activities.','Berlin', 'Tempelhof-Schöneberg', '12101', 'Tempelhofer Damm', '44', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.3900#map=16/52.4700/13.3900' ),
       (70, 'Bibliothek Lichtenberg Ost','Eastern Lichtenberg library focused on children’s literature and local history.','Berlin', 'Lichtenberg', '10367', 'Roedernallee', '22', 'https://www.openstreetmap.org/?mlat=52.5200&mlon=13.5200#map=16/52.5200/13.5200' ),
       (71, 'Bibliothek Hohenschönhausen Ost','Eastern Hohenschönhausen branch offering diverse media collections and study spaces.','Berlin', 'Lichtenberg', '13057', 'Wartenberger Straße', '80', 'https://www.openstreetmap.org/?mlat=52.5700&mlon=13.5300#map=16/52.5700/13.5300' ),
       (72, 'Bibliothek Friedrichshagen Ost','Eastern Friedrichshagen library with a focus on peaceful reading areas and community programs.','Berlin', 'Treptow-Köpenick', '12587', 'Rahnsdorfer Straße', '8', 'https://www.openstreetmap.org/?mlat=52.4800&mlon=13.6300#map=16/52.4800/13.6300' ),
       (73, 'Bibliothek Alt-Treptow Ost','Eastern Alt-Treptow library providing a rich selection of books and digital media.','Berlin', 'Friedrichshain-Kreuzberg', '12435', 'Plesser Straße', '22', 'https://www.openstreetmap.org/?mlat=52.4600&mlon=13.5600#map=16/52.4600/13.5600' ),
       (74, 'Bibliothek Wedding Ost','Eastern Wedding library with community-oriented programs and diverse media collections.','Berlin', 'Mitte', '13347', 'Rehberge', '25', 'https://www.openstreetmap.org/?mlat=52.5500&mlon=13.3500#map=16/52.5500/13.3500' ),
       (75, 'Bibliothek Spandau Ost','Eastern Spandau library focused on cultural events and educational resources.','Berlin', 'Spandau', '13597', 'Carl-Schurz-Straße', '42', 'https://www.openstreetmap.org/?mlat=52.5400&mlon=13.2000#map=16/52.5400/13.2000' ),
       (76, 'Bibliothek Reinickendorf Nord Ost','Northern Reinickendorf branch specializing in community education and digital access.','Berlin', 'Reinickendorf', '13405', 'Residenzstraße', '78', 'https://www.openstreetmap.org/?mlat=52.5800&mlon=13.3200#map=16/52.5800/13.3200' ),
       (77, 'Bibliothek Rudow Ost','Rudow neighborhood library with a wide array of books, media, and community events.','Berlin', 'Neukölln', '12357', 'Johannisthaler Chaussee', '210', 'https://www.openstreetmap.org/?mlat=52.4300&mlon=13.4400#map=16/52.4300/13.4400' ),
       (78, 'Bibliothek Lankwitz Ost','Lankwitz branch known for its welcoming atmosphere and diverse media collection.','Berlin', 'Steglitz-Zehlendorf', '12247', 'Lankwitzer Straße', '18', 'https://www.openstreetmap.org/?mlat=52.4500&mlon=13.3200#map=16/52.4500/13.3200' ),
       (79, 'Bibliothek Wilmersdorf Nord','Northern Wilmersdorf library offering a wide selection of literature and digital media.','Berlin', 'Charlottenburg-Wilmersdorf', '10715', 'Berliner Straße', '48', 'https://www.openstreetmap.org/?mlat=52.4900&mlon=13.2900#map=16/52.4900/13.2900' ),
       (80, 'Bibliothek Neukölln Nord','Northern Neukölln branch focused on youth programs and community engagement.','Berlin', 'Neukölln', '12051', 'Neuköllner Straße', '33', 'https://www.openstreetmap.org/?mlat=52.4900&mlon=13.4300#map=16/52.4900/13.4300' ),
       (81, 'Bibliothek Mitte Nord','Northern Mitte library with a broad range of books and cultural activities.','Berlin', 'Mitte', '10115', 'Alexanderplatz', '7', 'https://www.openstreetmap.org/?mlat=52.5200&mlon=13.4050#map=16/52.5200/13.4050' ),
       (82, 'Bibliothek Friedrichshain Nord','Northern Friedrichshain library providing community workshops and diverse media.','Berlin', 'Friedrichshain-Kreuzberg', '10243', 'Boxhagener Straße', '25', 'https://www.openstreetmap.org/?mlat=52.5100&mlon=13.4500#map=16/52.5100/13.4500' ),
       (83, 'Bibliothek Treptow Nord','Northern Treptow library offering educational resources and quiet study areas.','Berlin', 'Treptow-Köpenick', '12435', 'Sternstraße', '40', 'https://www.openstreetmap.org/?mlat=52.4700&mlon=13.5400#map=16/52.4700/13.5400' ),
       (84, 'Bibliothek Zehlendorf Nord','Northern Zehlendorf branch focused on literature for all ages and community events.','Berlin', 'Steglitz-Zehlendorf', '14163', 'Argentinische Allee', '35', 'https://www.openstreetmap.org/?mlat=52.4400&mlon=13.2700#map=16/52.4400/13.2700')
ON CONFLICT (library_id) DO NOTHING;
SELECT SETVAL('libraries_library_id_seq', (SELECT MAX(library_id) FROM libraries));

-- Items
INSERT INTO product_items (item_id, product_id, status_id)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 1, 1),
       (4, 1, 1),
       (5, 2, 2),
       (6, 2, 1),
       (7, 2, 2),
       (8, 3, 1),
       (9, 3, 1),
       (10, 3, 4),
       (11, 3, 2),
       (12, 3, 5),
       (13, 4, 1),
       (14, 4, 2),
       (15, 4, 2),
       (16, 5, 4),
       (17, 5, 1),
       (18, 5, 2),
       (19, 5, 1),
       (20, 6, 2),
       (21, 6, 2),
       (22, 6, 1),
       (23, 7, 1),
       (24, 7, 4),
       (25, 7, 4),
       (26, 7, 2),
       (27, 8, 1),
       (28, 8, 1),
       (29, 8, 2),
       (30, 9, 2),
       (31, 9, 5),
       (32, 9, 1),
       (33, 9, 2),
       (34, 10, 1),
       (35, 10, 2),
       (36, 10, 1),
       (37, 10, 1),
       (38, 10, 2),
       (39, 11, 4),
       (40, 11, 2),
       (41, 11, 1),
       (42, 11, 4),
       (43, 11, 5),
       (44, 11, 3),
       (45, 13, 3),
       -- available items
       (46, 13, 1),
       (47, 13, 1),
       -- test scenarios in borrowings
       (48, 13, 3),
       (49, 12, 3),
       (50, 11, 3),
       (51, 8, 3)
ON CONFLICT (item_id) DO NOTHING;
SELECT SETVAL('product_items_item_id_seq', (SELECT MAX(item_id) FROM product_items));

-- ItemLocation
INSERT INTO item_location (item_id, library_id, location_in_library)
VALUES (1, 1, 'Shelf A-12'),
       (2, 3, 'Room 2-17'),
       (3, 5, 'Shelf B-23'),
       (4, 2, 'Top Rack-8'),
       (5, 4, 'Shelf C-4'),
       (6, 1, 'Room 1-19'),
       (7, 2, 'Lower Shelf-5'),
       (8, 5, 'Shelf A-3'),
       (9, 3, 'Room 1-22'),
       (10, 4, 'Shelf B-10'),
       (11, 1, 'Shelf C-14'),
       (12, 2, 'Top Rack-7'),
       (13, 5, 'Room 2-25'),
       (14, 3, 'Shelf A-9'),
       (15, 4, 'Shelf C-16'),
       (16, 1, 'Room 1-5'),
       (17, 2, 'Shelf B-20'),
       (18, 5, 'Lower Shelf-12'),
       (19, 3, 'Top Rack-2'),
       (20, 4, 'Room 2-28'),
       (21, 1, 'Shelf C-6'),
       (22, 2, 'Room 1-13'),
       (23, 5, 'Room 2-10'),
       (24, 3, 'Shelf A-4'),
       (25, 4, 'Shelf B-19'),
       (26, 1, 'Room 1-9'),
       (27, 2, 'Shelf C-11'),
       (28, 5, 'Top Rack-1'),
       (29, 3, 'Room 2-7'),
       (30, 4, 'Lower Shelf-20'),
       (31, 1, 'Shelf B-15'),
       (32, 2, 'Room 1-6'),
       (33, 5, 'Shelf A-22'),
       (34, 3, 'Room 2-19'),
       (35, 4, 'Shelf C-9'),
       (36, 1, 'Shelf A-2'),
       (37, 2, 'Room 2-14'),
       (38, 5, 'Shelf B-17'),
       (39, 3, 'Lower Shelf-1'),
       (40, 4, 'Room 1-18'),
       (41, 5, 'Top Rack-9'),
       (42, 3, 'Lower Shelf-1'),
       (43, 4, 'Room 1-18'),
       (44, 5, 'Top Rack-9'),
       (45, 2, 'Next to the Staff Desk'),
       (46, 1, 'Main Library at the Entrance'),
       (47, 3, 'Top Rack-6'),
       (48, 5, 'Top Rack-9'),
       (49, 2, 'Next to the Staff Desk'),
       (50, 1, 'Main Library at the Entrance'),
       (51, 3, 'Top Rack-6')
ON CONFLICT (item_id) DO NOTHING;

--  ─────────── mock: BORROWINGS  ───────────
-- CASE 1: Active borrow (due in 3 days)
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 50, CURRENT_DATE - INTERVAL '25 days', CURRENT_DATE + INTERVAL '3 days', NULL, 0);

-- CASE 2: Overdue borrow (due 5 days ago)
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 49, CURRENT_DATE - INTERVAL '19 days', CURRENT_DATE - INTERVAL '5 days', NULL, 1);

-- CASE 3: Returned borrow (returned yesterday)
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 48, CURRENT_DATE - INTERVAL '8 days', CURRENT_DATE - INTERVAL '1 day', CURRENT_DATE - INTERVAL '1 day', 0);

-- CASE 4: Active borrow, max extensions
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 44, CURRENT_DATE - INTERVAL '42 days', CURRENT_DATE + INTERVAL '14 days', NULL, 2);

-- CASE 5: Returned early
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 45, CURRENT_DATE - INTERVAL '3 days', CURRENT_DATE + INTERVAL '4 days', CURRENT_DATE, 0);

-- CASE 6: Overdue, maxed out extensions
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (2, 51, CURRENT_DATE - INTERVAL '57 days', CURRENT_DATE - INTERVAL '1 day', NULL, 2);


SELECT SETVAL('borrows_borrow_id_seq', (SELECT MAX(borrow_id) FROM borrows));

--  ─────────── mock: RESERVATIONS ───────────
INSERT INTO reservations (reservation_id, custom_user_id, item_id, reservation_start, reservation_due)
VALUES
-- Starts today, ends in 3 days
(1, 2, 1, CURRENT_DATE, CURRENT_DATE + INTERVAL '3 days'),

-- Started yesterday, ends in 2 days
(2, 2, 2, CURRENT_DATE - INTERVAL '1 day', CURRENT_DATE + INTERVAL '2 days'),

-- Started 2 days ago, ends tomorrow
(3, 5, 3, CURRENT_DATE - INTERVAL '2 days', CURRENT_DATE + INTERVAL '1 day'),

-- Ends today
(4, 5, 3, CURRENT_DATE - INTERVAL '3 days', CURRENT_DATE),

-- Started 3 days ago, expired yesterday (simulate overdue for deletion job)
(5, 1, 44, CURRENT_DATE - INTERVAL '4 days', CURRENT_DATE - INTERVAL '1 day'),

-- Ends tomorrow
(6, 1, 20, CURRENT_DATE - INTERVAL '2 days', CURRENT_DATE + INTERVAL '1 day')
ON CONFLICT DO NOTHING;
SELECT SETVAL('reservations_reservation_id_seq', (SELECT MAX(reservation_id) FROM reservations));