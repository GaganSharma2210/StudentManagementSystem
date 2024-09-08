package com.manya.studentManagementSystem.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
		info=@Info(
				title = "Student Management System",
				description = "A system to Manage students Records",
				summary = "It consists of CRUD operation related to Students",
				termsOfService = "T & C",
				contact = @Contact(
						name = "Manya Rai",
						email = "manyaabc@gmail.com"
						),
				license = @License(
						name = "12345667"
						),
				version = "v1"
				))
@SecurityScheme(
		name="bearer",
		in = SecuritySchemeIn.HEADER,
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT",
		description ="JWT token"
		)
public class SwaggerConfig {

	
}