# Internship
Web application for managing departments & employees of a company. Company has several departments in different cities. CEO of the company can manage all departments & employees. On top of each department is a Head of Department, who can manage employees from his/her department.

Acceptance criteria:
- CEO can create/update/delete departments
-- department can be deleted only if there are no employees
- CEO can set min & max salary, an employee can earn in his department. Both values are optional.
- Head of department can create/disable employee accounts in his/her department
- Head of department can change his/her employee's password
- Head of department can set salary of his/her employees. Employee salary has to be between values set by CEO.
- REST API allows to search / list all employees (among all departments or within one department)
-- CEO and heads of departments can search within any department
-- An employee can search within his/her department
- A profile of an employee contains: first name, last name, several phone numbers (private, business), email address, department, salary, date of employment, whether his account is active or not, last login timestamp
- REST API allows to search / list all departments
-- CEO and head of departments can view all departments
-- Employee can view only his department
- A profile of a department contains: Name, city, head of department profile, number of employees, max & min salary, average salary, median of salary
- An employee can contact head of his/her department via email
- Head of department can send email to all employees of his/her department
- CEO can send email to all employees or to employees of particular department


Application must consist of 3 services (authentication, email & business)
Authentication service contains employee accounts.
Business service contains information about departments & it's employees.
Email service is responsible for sending emails (use FakeSMTP for testing) - https://github.com/Nilhcem/FakeSMTP

Authentication & business service are exposing REST API.
Business communicates with email service via RabbitMQ
Create docker images for each service
