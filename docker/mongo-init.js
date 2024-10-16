db.Character.insertOne(
  {
    "prefix": "Sr",
    "name": "Mario",
    "height": 2,
    "life": 2
  });
db.createUser({
  user: "uwebfluxdb",
  pwd: "pwebfluxdb",
  roles: [ "dbOwner" ]
});
