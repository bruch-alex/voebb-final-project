-- Product types
INSERT INTO product_types (product_type_id, name, is_digital) VALUES (1, 'book', false) ON CONFLICT (product_type_id) DO NOTHING;
INSERT INTO product_types (product_type_id, name, is_digital) VALUES (2, 'ebook', true) ON CONFLICT (product_type_id) DO NOTHING;
INSERT INTO product_types (product_type_id, name, is_digital) VALUES (3, 'DVD', false) ON CONFLICT (product_type_id) DO NOTHING;
SELECT setval('product_types_product_type_id_seq', (SELECT MAX(product_type_id) FROM product_types));

-- Item status
INSERT INTO item_status (item_status_id, item_status_name)
VALUES (1, 'available'),
       (2, 'reserved'),
       (3, 'borrowed'),
       (4, 'damaged'),
       (5, 'lost')
ON CONFLICT (item_status_id) DO NOTHING;
SELECT setval('item_status_item_status_id_seq', (SELECT MAX(item_status_id) FROM item_status));

--  Creator Roles
INSERT INTO creator_roles (creator_role_id, creator_role)
VALUES (1, 'author'),
       (2, 'co-author'),
       (3, 'editor'),
       (4, 'director');
SELECT setval('creator_roles_creator_role_id_seq', (SELECT MAX(creator_role_id) FROM creator_roles));

-- Client Roles
INSERT INTO user_roles (role_id, role_name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_CLIENT'),
       (3, 'ROLE_GUEST');

-- Languages  ─────────────────────────────────────────────────
INSERT INTO languages (language_id, language_name)
VALUES
    (1, 'English'),
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
SELECT setval('languages_language_id_seq', (SELECT MAX(language_id) FROM languages));
-- DUMMY DATA BELLOW

-- Products
INSERT INTO products (product_id, product_type_id, product_link_to_emedia, title, release_year, photo, description)
VALUES (1, 1, NULL, 'Harry Potter and the Philosopher''s Stone', '1997', 'photo_url_1','First book of Harry Potter series'),
       (2, 2, 'https://ebooks.voebb.de/hp1', 'Harry Potter and the Philosopher''s Stone', '1997', 'photo_url_1','First book of Harry Potter series'),
       (3, 1, NULL, 'Harry Potter and the Chamber of Secrets', '1998', 'photo_url_2', 'Second book in the series'),
       (4, 2, 'https://ebooks.voebb.de/hp2', 'Harry Potter and the Chamber of Secrets', '1998', 'photo_url_2','Second book in the series'),
       (5, 1, NULL, 'Harry Potter and the Prisoner of Azkaban', '1999', 'photo_url_3', 'Third book in the series'),
       (6, 2, 'https://ebooks.voebb.de/hp3', 'Harry Potter and the Goblet of Fire', '2000', 'photo_url_4','Fourth book in the series'),
       (7, 1, NULL, 'Harry Potter and the Order of the Phoenix', '2003', 'photo_url_5', 'Fifth book in the series'),
       (8, 2, 'https://ebooks.voebb.de/hp4', 'Harry Potter and the Half-Blood Prince', '2005', 'photo_url_6','Sixth book in the series'),
       (9, 1, NULL, 'Harry Potter and the Deathly Hallows', '2007', 'photo_url_7', 'Final book in the series'),
       (10, 1, NULL, 'Harry Potter and the Cursed Child', '2016', 'photo_url_8', 'Play based on Harry Potter universe'),
       (11, 1, NULL, 'Fantastic Beasts and Where to Find Them', '2001', 'photo_url_9','A companion book to Harry Potter'),
       (12, 3, NULL, 'The Matrix', '1999', 'photo_url_9','DVD format')
ON CONFLICT (product_id) DO NOTHING;
SELECT setval('products_product_id_seq', (SELECT MAX(product_id) FROM products));

-- Book Details (for both physical books and e-books)
INSERT INTO book_details (product_id, book_isbn, book_edition, book_pages)
VALUES (1, '9780747532699', '1st Edition', 223),
       (2, '9780747532699-E', 'Digital Edition', 223),
       (3, '9780747538493', '1st Edition', 251),
       (4, '9780747538493-E', 'Digital Edition', 251),
       (5, '9780747542155', '1st Edition', 317),
       (6, '9780747542155-E', 'Digital Edition', 317),
       (7, '9780747546245', '1st Edition', 607),
       (8, '9780747546245-E', 'Digital Edition', 607),
       (9, '9780747595830', '1st Edition', 607),
       (10, '9781338216660', '1st Edition', 320),
       (11, '9781408880715', '1st Edition', 128)
ON CONFLICT DO NOTHING;

-- Creators
INSERT INTO creators (creator_id, creator_first_name, creator_last_name)
VALUES (1, 'J. K.', 'Rowling'),
       (2, 'John', 'Tiffany'),
       (3, 'Jack', 'Thorne')
ON CONFLICT (creator_id) DO NOTHING;
SELECT setval('creators_creator_id_seq', (SELECT MAX(creator_id) FROM creators));

-- Join table (same Creator ->  multiple  Roles; same Product -> multiple Creators)
INSERT INTO creator_product_relation (creator_id, product_id, creator_role_id)
VALUES
    -- Rowling on both books, different roles
    (1, 10, 1), -- AUTHOR on Cursed Child   (already there)
    (1, 11, 2), -- CO_AUTHOR on Fantastic Beasts

    -- Cursed Child still has two co-authors
    (2, 10, 2), -- Tiffany  CO_AUTHOR
    (3, 10, 2); -- Thorne   CO_AUTHOR

--  Clients ─────────────────────────────────────────────────
INSERT INTO custom_users (first_name, last_name, email, password, is_enabled, borrowed_books_count)
VALUES ('User 1', 'One', 'test@gmail.com', '1234', true, 0),
       ('User 2 ', 'Two', 'test@example.com', '1234', true, 0)
ON CONFLICT (custom_user_id) DO NOTHING;

-- Join table (same Client -> multiple Roles)
INSERT INTO users_roles_relation (custom_user_id, role_id)
VALUES (1, 1),
       (1, 2);

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
SELECT setval('countries_country_id_seq', (SELECT MAX(country_id) FROM countries));

-- Join table (product linked to countries)
INSERT INTO country_relation (product_id, country_id)
VALUES (1, 1),  -- Philosopher's Stone published in the UK
       (1, 2),  -- ... and in the US
       (10, 1), -- Cursed Child in the UK
       (11, 2);  -- Fantastic Beasts in the US

-- Join table (product linked to languages)
INSERT INTO language_relation (product_id, language_id)
VALUES (1, 1),
       (1, 2)
ON CONFLICT (product_id, language_id) DO NOTHING;

-- Libraries ─────────────────────────────────────────────────
INSERT INTO libraries (library_id, library_name, library_description, address_city, address_district, address_postcode, address_street, address_house_nr, address_osm_link) VALUES (1, 'Central City Library', 'Main public library in the heart of Berlin.', 'Berlin', 'Mitte', '10115', 'Hauptstraße', '123', 'https://www.openstreetmap.org/?mlat=52.5300&mlon=13.3847#map=16/52.5300/13.3847') ON CONFLICT (library_id) DO NOTHING;
INSERT INTO libraries (library_id, library_name, library_description, address_city, address_district, address_postcode, address_street, address_house_nr, address_osm_link) VALUES (2, 'West End Library', 'A cozy library located in the Charlottenburg district.', 'Berlin', 'Charlottenburg-Wilmersdorf', '10623', 'Kaiser-Wilhelm-Straße', '45', 'https://www.openstreetmap.org/?mlat=52.5150&mlon=13.2900#map=16/52.5150/13.2900') ON CONFLICT (library_id) DO NOTHING;
INSERT INTO libraries (library_id, library_name, library_description, address_city, address_district, address_postcode, address_street, address_house_nr, address_osm_link) VALUES (3, 'East Side Library', 'Library near the East Side Gallery, offering a variety of books.', 'Berlin', 'Friedrichshain-Kreuzberg', '10243', 'Mühlenstraße', '10', 'https://www.openstreetmap.org/?mlat=52.5074&mlon=13.4396#map=16/52.5074/13.4396') ON CONFLICT (library_id) DO NOTHING;
INSERT INTO libraries (library_id, library_name, library_description, address_city, address_district, address_postcode, address_street, address_house_nr, address_osm_link) VALUES (4, 'North Gate Library', 'A library situated in the Reinickendorf district, known for its quiet reading rooms.', 'Berlin', 'Reinickendorf', '13407', 'Wilhelmsruher Damm', '23', 'https://www.openstreetmap.org/?mlat=52.5960&mlon=13.2905#map=16/52.5960/13.2905') ON CONFLICT (library_id) DO NOTHING;
INSERT INTO libraries (library_id, library_name, library_description, address_city, address_district, address_postcode, address_street, address_house_nr, address_osm_link) VALUES (5, 'South Park Library', 'Located near the Tempelhofer Feld, offering a peaceful environment for reading.', 'Berlin', 'Tempelhof-Schöneberg', '12103', 'Tempelhofer Damm', '89', 'https://www.openstreetmap.org/?mlat=52.4706&mlon=13.3989#map=16/52.4706/13.3989') ON CONFLICT (library_id) DO NOTHING;
SELECT setval('libraries_library_id_seq', (SELECT MAX(library_id) FROM libraries));

-- Items
INSERT INTO product_items (item_id, product_id, status_id) VALUES (1, 1, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (2, 1, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (3, 1, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (4, 1, 3) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (5, 2, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (6, 2, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (7, 2, 2) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (8, 3, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (9, 3, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (10, 3, 3) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (11, 3, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (12, 3, 3) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (13, 4, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (14, 4, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (15, 4, 2) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (16, 5, 3) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (17, 5, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (18, 5, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (19, 5, 1) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (20, 6, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (21, 6, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (22, 6, 1) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (23, 7, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (24, 7, 3) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (25, 7, 3) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (26, 7, 2) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (27, 8, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (28, 8, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (29, 8, 2) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (30, 9, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (31, 9, 3) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (32, 9, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (33, 9, 2) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (34, 10, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (35, 10, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (36, 10, 3) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (37, 10, 1) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (38, 10, 2) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (39, 11, 3) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (40, 11, 2) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (41, 11, 1) ON CONFLICT (item_id) DO NOTHING;

INSERT INTO product_items (item_id, product_id, status_id) VALUES (42, 11, 4) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (43, 11, 5) ON CONFLICT (item_id) DO NOTHING;
INSERT INTO product_items (item_id, product_id, status_id) VALUES (44, 11, 5) ON CONFLICT (item_id) DO NOTHING;

SELECT setval('product_items_item_id_seq', (SELECT MAX(item_id) FROM product_items));

-- ItemLocation
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (1, 1, 'Shelf A-12') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (2, 3, 'Room 2-17') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (3, 5, 'Shelf B-23') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (4, 2, 'Top Rack-8') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (5, 4, 'Shelf C-4') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (6, 1, 'Room 1-19') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (7, 2, 'Lower Shelf-5') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (8, 5, 'Shelf A-3') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (9, 3, 'Room 1-22') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (10, 4, 'Shelf B-10') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (11, 1, 'Shelf C-14') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (12, 2, 'Top Rack-7') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (13, 5, 'Room 2-25') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (14, 3, 'Shelf A-9') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (15, 4, 'Shelf C-16') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (16, 1, 'Room 1-5') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (17, 2, 'Shelf B-20') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (18, 5, 'Lower Shelf-12') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (19, 3, 'Top Rack-2') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (20, 4, 'Room 2-28') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (21, 1, 'Shelf C-6') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (22, 2, 'Room 1-13') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (23, 5, 'Room 2-10') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (24, 3, 'Shelf A-4') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (25, 4, 'Shelf B-19') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (26, 1, 'Room 1-9') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (27, 2, 'Shelf C-11') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (28, 5, 'Top Rack-1') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (29, 3, 'Room 2-7') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (30, 4, 'Lower Shelf-20') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (31, 1, 'Shelf B-15') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (32, 2, 'Room 1-6') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (33, 5, 'Shelf A-22') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (34, 3, 'Room 2-19') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (35, 4, 'Shelf C-9') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (36, 1, 'Shelf A-2') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (37, 2, 'Room 2-14') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (38, 5, 'Shelf B-17') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (39, 3, 'Lower Shelf-1') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (40, 4, 'Room 1-18') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (41, 5, 'Top Rack-9') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (42, 3, 'Lower Shelf-1') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (43, 4, 'Room 1-18') ON CONFLICT (item_id) DO NOTHING;
INSERT INTO item_location (item_id, library_id, location_in_library) VALUES (44, 5, 'Top Rack-9') ON CONFLICT (item_id) DO NOTHING;

--  ─────────── mock: client_id = 1 borrows item_id = 1 ───────────
INSERT INTO borrows (borrow_id, custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 1, 1,CURRENT_DATE, CURRENT_DATE + INTERVAL '14 day',NULL, 0) ON CONFLICT DO NOTHING;
SELECT setval('borrows_borrow_id_seq', (SELECT MAX(borrow_id) FROM borrows));

--  ─────────── mock: client_id = 1 reserves item_id = 1 ───────────
INSERT INTO reservations (reservation_id, custom_user_id, item_id, reservation_start, reservation_due)
VALUES (1, 2, 1, CURRENT_DATE, CURRENT_DATE + INTERVAL '7 day') ON CONFLICT DO NOTHING;
SELECT setval('reservations_reservation_id_seq', (SELECT MAX(item_id) FROM reservations));