CREATE TABLE scheduler_job_info
(
    job_id           int NOT NULL,
    job_date         date NOT NULL,
    job_time         time NOT NULL,
    job_device_id     VARCHAR(120) NOT NULL,
    job_action_type   VARCHAR(120) NOT NULL,
    PRIMARY KEY (job_id)
);
