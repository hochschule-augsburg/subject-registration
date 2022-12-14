DROP TABLE IF EXISTS hsa_registration_window;
DROP TABLE IF EXISTS hsa_registration;
DROP TABLE IF EXISTS hsa_subject;
DROP TABLE IF EXISTS hsa_subject_selection;

CREATE TABLE hsa_registration_window (
 id BINARY(16)  NOT NULL,
 semester VARCHAR(30) NOT NULL,
 start_date timestamp NOT NULL,
 end_date timestamp NOT NULL,
 status VARCHAR(30) NOT NULL,
 PRIMARY KEY (id)
);

CREATE TABLE hsa_registration (
  id BINARY(16) NOT NULL,
  student VARCHAR(50) NOT NULL,
  registration_window_id BINARY(16) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (registration_window_id) REFERENCES hsa_registration_window(id)
);

CREATE TABLE hsa_subject(
 id BINARY(16) NOT NULL,
 name VARCHAR(70) NOT NULL,
 professor VARCHAR(100) NOT NULL,
 cp REAL NOT NULL,
 description VARCHAR(1500) NOT NULL,
 specialization VARCHAR(150) NOT NULL,
 status boolean NOT NULL,
 capacity INTEGER NOT NULL,
 PRIMARY KEY (id)

);

CREATE TABLE hsa_subject_selection (
 id BINARY(16) NOT NULL,
 registration_id BINARY(16) NOT NULL,
 subject_id BINARY(16) NOT NULL,
 points INTEGER NOT NULL,
 PRIMARY KEY (id),
 FOREIGN KEY (subject_id) REFERENCES hsa_subject(id),
 FOREIGN KEY (registration_id) REFERENCES hsa_registration(id)
);






