// Switch to your document-based microservice database
db = db.getSiblingDB('UserDB');

// Explicitly build the default 'users' collection
db.createCollection('users');

// Seed an initial setup marker document
db.users.insertOne({
    _id: "system_init",
    status: "active",
    description: "Database initialized successfully",
    createdAt: new Date()
});