FROM node:current-alpine3.12

# create destination directory
RUN mkdir -p /usr/src/nuxt-app
WORKDIR /usr/src/nuxt-app

# copy the app, note .dockerignore
COPY . /usr/src/nuxt-app/
RUN yarn install

# build necessary, even if no static files are needed,
# since it builds the server as well
RUN yarn build

# expose 5000 on container
EXPOSE 3000

# set app serving to permissive / assigned
ENV NUXT_HOST=0.0.0.0
ENV hostname=0.0.0.0
# set app port
ENV NUXT_PORT=3000

# start the app
CMD [ "yarn", "start" ]
