const admin = require('firebase-admin');

// Initialize Firebase Admin SDK
// Note: Download serviceAccountKey.json from Firebase Console -> Project Settings -> Service Accounts
admin.initializeApp({
  credential: admin.credential.cert('./serviceAccountKey.json'),
  databaseURL: 'https://kinibusapps-xxxxx.firebaseio.com' // Replace with your project ID
});

const db = admin.firestore();
const timestamp = admin.firestore.Timestamp;

// Comprehensive Bus Seed Data for KiniBusApps
const busSeedData = [
  // === JAKARTA → BANDUNG ROUTES ===
  {
    nama: "KiniShuttle Executive",
    jenis: "EXECUTIVE",
    kapasitas: 50,
    harga: 150000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T06:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T09:30:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 45,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "USB Charging", "Entertainment"],
    deskripsi: "Luxury executive bus with premium amenities"
  },
  {
    nama: "Bandung Premium Express",
    jenis: "BUSINESS",
    kapasitas: 42,
    harga: 125000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T07:15:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T10:45:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 40,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Snacks", "Blanket"],
    deskripsi: "Business class with excellent service"
  },
  {
    nama: "Economy Rider Plus",
    jenis: "ECONOMIC",
    kapasitas: 55,
    harga: 95000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T08:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T12:00:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 52,
    tersedia: true,
    fasilitas: ["AC", "Fan", "Basic Amenities"],
    deskripsi: "Affordable travel with essential comforts"
  },
  {
    nama: "Bandung Night Express",
    jenis: "EXECUTIVE",
    kapasitas: 48,
    harga: 160000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T22:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-14T01:30:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 46,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Dinner", "Sleeping Seats"],
    deskripsi: "Overnight service with dinner included"
  },

  // === JAKARTA → BOGOR ROUTES ===
  {
    nama: "Bogor AC Express",
    jenis: "EXECUTIVE",
    kapasitas: 45,
    harga: 85000,
    keberangkatan: "Gambir",
    tujuan: "Pasteur",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T06:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T07:45:00Z")),
    durasi: "1h 45m",
    kursiTersedia: 43,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Coffee"],
    deskripsi: "Quick and comfortable city route"
  },
  {
    nama: "Bogor Morning Shuttle",
    jenis: "BUSINESS",
    kapasitas: 40,
    harga: 70000,
    keberangkatan: "Gambir",
    tujuan: "Pasteur",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T07:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T09:15:00Z")),
    durasi: "1h 45m",
    kursiTersedia: 38,
    tersedia: true,
    fasilitas: ["AC", "WiFi"],
    deskripsi: "Business hours service"
  },
  {
    nama: "Bogor Budget Line",
    jenis: "ECONOMIC",
    kapasitas: 50,
    harga: 55000,
    keberangkatan: "Gambir",
    tujuan: "Pasteur",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T08:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T09:45:00Z")),
    durasi: "1h 45m",
    kursiTersedia: 48,
    tersedia: true,
    fasilitas: ["AC", "Fan"],
    deskripsi: "Budget-friendly option"
  },

  // === JAKARTA → CIREBON ROUTES ===
  {
    nama: "Cirebon Luxury Coach",
    jenis: "EXECUTIVE",
    kapasitas: 46,
    harga: 180000,
    keberangkatan: "Pulo Gebang",
    tujuan: "Cicaheum",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T05:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T09:45:00Z")),
    durasi: "4h 15m",
    kursiTersedia: 44,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Lunch", "Entertainment"],
    deskripsi: "Long distance luxury travel"
  },
  {
    nama: "Cirebon Express Plus",
    jenis: "BUSINESS",
    kapasitas: 42,
    harga: 140000,
    keberangkatan: "Pulo Gebang",
    tujuan: "Cicaheum",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T07:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T11:15:00Z")),
    durasi: "4h 15m",
    kursiTersedia: 40,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Snacks"],
    deskripsi: "Business class for longer journeys"
  },
  {
    nama: "Cirebon Economy Line",
    jenis: "ECONOMIC",
    kapasitas: 52,
    harga: 110000,
    keberangkatan: "Pulo Gebang",
    tujuan: "Cicaheum",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T08:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T12:45:00Z")),
    durasi: "4h 15m",
    kursiTersedia: 50,
    tersedia: true,
    fasilitas: ["AC", "Basic Amenities"],
    deskripsi: "Affordable long distance travel"
  },

  // === BANDUNG → YOGYAKARTA ROUTES ===
  {
    nama: "Borobudur Heritage",
    jenis: "EXECUTIVE",
    kapasitas: 44,
    harga: 250000,
    keberangkatan: "Lw. Panjang",
    tujuan: "Cihampelas",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T04:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T10:30:00Z")),
    durasi: "6h 30m",
    kursiTersedia: 42,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Meals", "Guide"],
    deskripsi: "Cultural heritage route with meals"
  },
  {
    nama: "Java Island Explorer",
    jenis: "BUSINESS",
    kapasitas: 38,
    harga: 200000,
    keberangkatan: "Lw. Panjang",
    tujuan: "Cihampelas",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T05:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T12:00:00Z")),
    durasi: "6h 30m",
    kursiTersedia: 36,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Snacks", "Rest Stops"],
    deskripsi: "Explore Java with comfort"
  },
  {
    nama: "Yogya Budget Express",
    jenis: "ECONOMIC",
    kapasitas: 48,
    harga: 160000,
    keberangkatan: "Lw. Panjang",
    tujuan: "Cihampelas",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T07:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T13:30:00Z")),
    durasi: "6h 30m",
    kursiTersedia: 45,
    tersedia: true,
    fasilitas: ["AC", "Basic Meals"],
    deskripsi: "Long distance budget option"
  },

  // === SURABAYA → MALANG ROUTES ===
  {
    nama: "Malang Adventure Plus",
    jenis: "EXECUTIVE",
    kapasitas: 42,
    harga: 95000,
    keberangkatan: "Plaza Senayan",
    tujuan: "Gedebage",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T07:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T09:30:00Z")),
    durasi: "2h 30m",
    kursiTersedia: 40,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Snacks"],
    deskripsi: "Adventure ready executive service"
  },
  {
    nama: "East Java Business",
    jenis: "BUSINESS",
    kapasitas: 40,
    harga: 80000,
    keberangkatan: "Plaza Senayan",
    tujuan: "Gedebage",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T08:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T11:00:00Z")),
    durasi: "2h 30m",
    kursiTersedia: 38,
    tersedia: true,
    fasilitas: ["AC", "WiFi"],
    deskripsi: "Business travel between major cities"
  },
  {
    nama: "Malang Economy Rider",
    jenis: "ECONOMIC",
    kapasitas: 50,
    harga: 65000,
    keberangkatan: "Plaza Senayan",
    tujuan: "Gedebage",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T09:15:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T11:45:00Z")),
    durasi: "2h 30m",
    kursiTersedia: 47,
    tersedia: true,
    fasilitas: ["AC", "Fan"],
    deskripsi: "Budget option for short trips"
  },

  // === ADDITIONAL ROUTES FOR COVERAGE ===
  // Jakarta → Semarang
  {
    nama: "Semarang Heritage Bus",
    jenis: "EXECUTIVE",
    kapasitas: 44,
    harga: 190000,
    keberangkatan: "Lebak Bulus",
    tujuan: "Cihampelas",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T06:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T12:00:00Z")),
    durasi: "5h 30m",
    kursiTersedia: 42,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Lunch", "Cultural Guide"],
    deskripsi: "Heritage route with local culture insights"
  },

  // Bandung → Surabaya
  {
    nama: "Trans Java Express",
    jenis: "BUSINESS",
    kapasitas: 40,
    harga: 220000,
    keberangkatan: "Lw. Panjang",
    tujuan: "Gedebage",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T05:45:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T15:30:00Z")),
    durasi: "9h 45m",
    kursiTersedia: 38,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Meals", "Rest Areas"],
    deskripsi: "Cross-island business travel"
  },

  // Yogyakarta → Bali (Special Route)
  {
    nama: "Bali Discovery Tour",
    jenis: "EXECUTIVE",
    kapasitas: 38,
    harga: 280000,
    keberangkatan: "Cihampelas",
    tujuan: "Pasteur",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-14T04:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-14T12:00:00Z")),
    durasi: "8h 0m",
    kursiTersedia: 35,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Meals", "Tour Guide", "Hotel Transfer"],
    deskripsi: "Complete tour package with hotel transfer"
  },

  // === MULTI-DAY SCHEDULES (13, 14, 15 Jan 2026) ===

  // === 13 JAN 2026 (TODAY) ===
  // Jakarta → Bandung - 13 Jan
  {
    nama: "Morning Express",
    jenis: "EXECUTIVE",
    kapasitas: 50,
    harga: 150000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T06:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T10:00:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 45,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Breakfast"],
    deskripsi: "Early morning executive service with breakfast"
  },
  {
    nama: "Business Shuttle",
    jenis: "BUSINESS",
    kapasitas: 40,
    harga: 125000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T08:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T11:30:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 38,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Newspaper"],
    deskripsi: "Business class morning service"
  },
  {
    nama: "Economy Morning",
    jenis: "ECONOMIC",
    kapasitas: 55,
    harga: 95000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T09:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T13:00:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 52,
    tersedia: true,
    fasilitas: ["AC", "Fan"],
    deskripsi: "Budget morning departure"
  },

  // Jakarta → Bogor - 13 Jan
  {
    nama: "Bogor Express Morning",
    jenis: "EXECUTIVE",
    kapasitas: 45,
    harga: 85000,
    keberangkatan: "Gambir",
    tujuan: "Pasteur",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T07:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T08:30:00Z")),
    durasi: "1h 30m",
    kursiTersedia: 43,
    tersedia: true,
    fasilitas: ["AC", "WiFi"],
    deskripsi: "Premium Bogor service"
  },
  {
    nama: "Bogor Business",
    jenis: "BUSINESS",
    kapasitas: 40,
    harga: 70000,
    keberangkatan: "Gambir",
    tujuan: "Pasteur",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-13T08:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-13T10:00:00Z")),
    durasi: "1h 30m",
    kursiTersedia: 38,
    tersedia: true,
    fasilitas: ["AC", "WiFi"],
    deskripsi: "Business morning to Bogor"
  },

  // === 14 JAN 2026 (TOMORROW) ===
  // Jakarta → Bandung - 14 Jan
  {
    nama: "Tomorrow Express",
    jenis: "EXECUTIVE",
    kapasitas: 50,
    harga: 140000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-14T07:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-14T10:30:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 48,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Snacks"],
    deskripsi: "Tomorrow's special route"
  },
  {
    nama: "Afternoon Shuttle",
    jenis: "BUSINESS",
    kapasitas: 40,
    harga: 115000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-14T13:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-14T16:30:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 38,
    tersedia: true,
    fasilitas: ["AC", "WiFi"],
    deskripsi: "Afternoon business class"
  },
  {
    nama: "Evening Budget",
    jenis: "ECONOMIC",
    kapasitas: 55,
    harga: 85000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-14T17:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-14T20:30:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 50,
    tersedia: true,
    fasilitas: ["AC", "Fan"],
    deskripsi: "Evening budget option"
  },

  // Jakarta → Bogor - 14 Jan
  {
    nama: "Bogor Afternoon",
    jenis: "BUSINESS",
    kapasitas: 35,
    harga: 65000,
    keberangkatan: "Gambir",
    tujuan: "Pasteur",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-14T14:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-14T15:30:00Z")),
    durasi: "1h 30m",
    kursiTersedia: 33,
    tersedia: true,
    fasilitas: ["AC", "WiFi"],
    deskripsi: "Afternoon Bogor service"
  },

  // === 15 JAN 2026 (DAY AFTER) ===
  // Jakarta → Bandung - 15 Jan
  {
    nama: "Weekend Express",
    jenis: "EXECUTIVE",
    kapasitas: 50,
    harga: 160000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-15T08:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-15T11:30:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 46,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Lunch"],
    deskripsi: "Weekend luxury service"
  },
  {
    nama: "Weekend Business",
    jenis: "BUSINESS",
    kapasitas: 40,
    harga: 130000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-15T09:30:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-15T13:00:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 38,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Entertainment"],
    deskripsi: "Weekend business travel"
  },
  {
    nama: "Weekend Economy",
    jenis: "ECONOMIC",
    kapasitas: 55,
    harga: 100000,
    keberangkatan: "Kp. Rambutan",
    tujuan: "Lw. Panjang",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-15T11:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-15T14:30:00Z")),
    durasi: "3h 30m",
    kursiTersedia: 52,
    tersedia: true,
    fasilitas: ["AC", "Fan", "Music"],
    deskripsi: "Weekend budget option"
  },

  // Jakarta → Bogor - 15 Jan
  {
    nama: "Weekend Bogor",
    jenis: "EXECUTIVE",
    kapasitas: 45,
    harga: 90000,
    keberangkatan: "Gambir",
    tujuan: "Pasteur",
    waktuKeberangkatan: timestamp.fromDate(new Date("2026-01-15T10:00:00Z")),
    waktuTiba: timestamp.fromDate(new Date("2026-01-15T11:30:00Z")),
    durasi: "1h 30m",
    kursiTersedia: 43,
    tersedia: true,
    fasilitas: ["AC", "WiFi", "Refreshments"],
    deskripsi: "Weekend Bogor luxury"
  }
];

// Sample user data for testing
const userSeedData = [
  {
    email: "admin@kinibus.com",
    nama: "Admin KiniBus",
    nomorTelepon: "+6281234567890",
    profileImageUrl: "",
    isActive: true,
    createdAt: timestamp.now(),
    updatedAt: timestamp.now()
  },
  {
    email: "user@kinibus.com",
    nama: "Test User",
    nomorTelepon: "+6281234567891",
    profileImageUrl: "",
    isActive: true,
    createdAt: timestamp.now(),
    updatedAt: timestamp.now()
  }
];

// Function to seed all data
async function seedDatabase() {
  try {
    console.log('🚀 Starting database seeding...');

    // Clear existing data first (optional - uncomment if needed)
    // await clearExistingData();

    // Seed bus data
    console.log('📋 Seeding bus data...');
    const busBatch = db.batch();
    busSeedData.forEach((bus) => {
      const docRef = db.collection('buses').doc();
      busBatch.set(docRef, {
        ...bus,
        createdAt: timestamp.now(),
        updatedAt: timestamp.now()
      });
    });
    await busBatch.commit();
    console.log(`✅ Successfully seeded ${busSeedData.length} bus records`);

    // Seed user data
    console.log('👤 Seeding user data...');
    const userBatch = db.batch();
    userSeedData.forEach((user) => {
      const docRef = db.collection('users').doc();
      userBatch.set(docRef, user);
    });
    await userBatch.commit();
    console.log(`✅ Successfully seeded ${userSeedData.length} user records`);

    console.log('🎉 Database seeding completed successfully!');
    console.log('\n📊 Summary:');
    console.log(`   • ${busSeedData.length} bus routes across ${getUniqueRoutes().length} routes`);
    console.log(`   • ${getBusClassCount().executive} Executive buses`);
    console.log(`   • ${getBusClassCount().business} Business buses`);
    console.log(`   • ${getBusClassCount().economic} Economic buses`);
    console.log(`   • ${userSeedData.length} sample users`);

  } catch (error) {
    console.error('❌ Error seeding database:', error);
    throw error;
  }
}

// Helper function to clear existing data (use with caution!)
async function clearExistingData() {
  console.log('🧹 Clearing existing data...');

  const collections = ['buses', 'users', 'penyewaan', 'eTickets'];

  for (const collectionName of collections) {
    const snapshot = await db.collection(collectionName).get();
    const batch = db.batch();

    snapshot.docs.forEach((doc) => {
      batch.delete(doc.ref);
    });

    await batch.commit();
    console.log(`   Cleared ${snapshot.size} documents from ${collectionName}`);
  }
}

// Helper functions for statistics
function getUniqueRoutes() {
  const routes = new Set();
  busSeedData.forEach(bus => {
    routes.add(`${bus.keberangkatan} → ${bus.tujuan}`);
  });
  return Array.from(routes);
}

function getBusClassCount() {
  return busSeedData.reduce((acc, bus) => {
    if (bus.jenis === 'EXECUTIVE') acc.executive++;
    else if (bus.jenis === 'BUSINESS') acc.business++;
    else if (bus.jenis === 'ECONOMIC') acc.economic++;
    return acc;
  }, { executive: 0, business: 0, economic: 0 });
}

// Execute seeding
if (require.main === module) {
  seedDatabase()
    .then(() => {
      console.log('\n🎯 Next steps:');
      console.log('1. Run your Android app');
      console.log('2. Test the bus listing in Dashboard');
      console.log('3. Try filtering by class and sorting');
      console.log('4. Test booking functionality');
      process.exit(0);
    })
    .catch((error) => {
      console.error('Seeding failed:', error);
      process.exit(1);
    });
}

module.exports = { seedDatabase, busSeedData, userSeedData };
