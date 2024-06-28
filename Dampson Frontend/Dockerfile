# Use an official Node.js image as the base image
FROM node:16-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and package-lock.json files to the working directory
COPY package.json package-lock.json ./

# Install the dependencies
RUN npm install

# Copy the rest of the application files to the working directory
COPY . .

# Set environment variables (optional, can also be set in docker-compose)
ENV REACT_APP_EMAILJS_SERVICE_ID=service_9uk09ea
ENV REACT_APP_EMAILJS_TEMPLATE_ID=template_ey2ugrk
ENV REACT_APP_EMAILJS_USER_ID=4poZ_Sxf-G8tmjq9X
ENV REACT_APP_EMAILJS_SERVICE_ID1=service_jecpwr6
ENV REACT_APP_EMAILJS_TEMPLATE_ID1=template_7kba0iw
ENV REACT_APP_EMAILJS_USER_ID1=-XQ7vmI579XvtO4m_
ENV REACT_APP_API_URL=http://15.206.139.27:7010

# Expose the port the app runs on
EXPOSE 7010 
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
