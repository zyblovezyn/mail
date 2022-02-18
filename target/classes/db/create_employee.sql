CREATE TABLE [Staff_Status]
(
    [employee_id] NVARCHAR(50),
    [Name] NVARCHAR(50),
    [Email] NVARCHAR(60),
    [Status] NVARCHAR(70)
);

SELECT  employee_id,name,email,status  FROM staff_status

CREATE TABLE [salary]
(
    [employee_id] NVARCHAR(50),
    [Name] NVARCHAR(50),
    [Email] NVARCHAR(60)
);

CREATE TABLE [bonus]
(
    [employee_id] NVARCHAR(50),
    [Name] NVARCHAR(50),
    [Email] NVARCHAR(60)
);

CREATE TABLE [source_Ticket]
(
    [employee_id] NVARCHAR(50),
    [Name] NVARCHAR(50),
    [Email] NVARCHAR(60)
);

INSERT INTO salary (employee_id,Name,Email) VALUES
	 ('0001','’£•£”Ž1','pual.office@gmail.com'),
	 ('0002','’£•£”Ž2','zhangyuanbo12358@163.com'),
	 ('0003','’£•£”Ž3','love.zyb@outlook.com'),
	 ('0004','’£•£”Ž4','zhang.yuan.bo.love@gmail.com'),
	 ('0005','ŒÓ—Y','hu.xionggang@smartcompany.jp');

INSERT INTO bonus (employee_id,Name,Email) VALUES
	 ('0001','’£•£”Ž1','pual.office@gmail.com'),
	 ('0002','’£•£”Ž2','zhangyuanbo12358@163.com'),
	 ('0003','’£•£”Ž3','love.zyb@outlook.com'),
	 ('0004','’£•£”Ž4','zhang.yuan.bo.love@gmail.com'),
	 ('0005','ŒÓ—Y','hu.xionggang@smartcompany.jp');

INSERT INTO source_Ticket (employee_id,Name,Email) VALUES
	 ('0001','’£•£”Ž1','pual.office@gmail.com'),
	 ('0002','’£•£”Ž2','zhangyuanbo12358@163.com'),
	 ('0003','’£•£”Ž3','love.zyb@outlook.com'),
	 ('0004','’£•£”Ž4','zhang.yuan.bo.love@gmail.com'),
	 ('0005','ŒÓ—Y?','hu.xionggang@smartcompany.jp');