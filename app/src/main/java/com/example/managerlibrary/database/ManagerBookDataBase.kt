package com.example.managerlibrary.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ManagerBookDataBase(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "LibraryDataBase.db"

        // Table names
        const val BOOK_TABLE_NAME = "Book"
        const val MEMBER_TABLE_NAME = "Member"
        const val LIBRARIAN_TABLE_NAME = "Librarian"
        const val LOAN_SLIP_TABLE_NAME = "LibraryLoanSlip"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val librarianTable = "CREATE TABLE Librarian( " +
                "librarianID TEXT PRIMARY KEY," +
                " librarianName TEXT NOT NULL, " +
                "password TEXT," +
                " role TEXT NOT NULL" +
                ")"
        db?.execSQL(librarianTable)

        //create 3 data for librarian
        db?.execSQL("INSERT INTO Librarian VALUES('admin','admin','admin','admin')")
        db?.execSQL("INSERT INTO Librarian VALUES('librarian','librarian','librarian','librarian')")
        db?.execSQL("INSERT INTO Librarian VALUES('librarian1','librarian1','librarian1','librarian')")


        val categoryBook = "CREATE TABLE CategoryBook( " +
                "categoryID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " categoryName TEXT NOT NULL" +
                ")"
        db?.execSQL(categoryBook)

        //create 3 data for category book
        db?.execSQL("INSERT INTO CategoryBook VALUES(1,'Sách văn học')")
        db?.execSQL("INSERT INTO CategoryBook VALUES(2,'Sách khoa học')")
        db?.execSQL("INSERT INTO CategoryBook VALUES(3,'Sách kinh tế')")

        val bookTable = "CREATE TABLE Book( " +
                "bookID INTEGER PRIMARY KEY," +
                " bookName TEXT NOT NULL, " +
                "rentalFee INTEGER NOT NULL," +
                " categoryID INTEGER NOT NULL," +
                "FOREIGN KEY (categoryID) REFERENCES CategoryBook(categoryID)" +
                ")"
        db?.execSQL(bookTable)

        //create 3 data for book
        db?.execSQL("INSERT INTO Book VALUES(1,'Sách văn học 1',10000,1)")
        db?.execSQL("INSERT INTO Book VALUES(2,'Sách khoa học 1',20000,2)")
        db?.execSQL("INSERT INTO Book VALUES(3,'Sách kinh tế 1',30000,3)")

        val memberTable = "CREATE TABLE Member( " +
                "memberID INTEGER PRIMARY KEY," +
                " memberName TEXT NOT NULL, " +
                "birthYear TEXT NOT NULL" +
                ")"
        db?.execSQL(memberTable)

        //create 3 data for member
        db?.execSQL("INSERT INTO Member VALUES(1,'Nguyễn Văn A','1999')")
        db?.execSQL("INSERT INTO Member VALUES(2,'Nguyễn Văn B','1998')")
        db?.execSQL("INSERT INTO Member VALUES(3,'Nguyễn Văn C','1997')")

        val libraryLoanSlip = "CREATE TABLE LibraryLoanSlip( " +
                "loanSlipID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " librarianID TEXT NOT NULL, " +
                "memberID INTEGER NOT NULL," +
                " bookID INTEGER NOT NULL," +
                " loanDate TEXT NOT NULL," +
                "status INTEGER NOT NULL," +
                "FOREIGN KEY (librarianID) REFERENCES Librarian(librarianID)," +
                "FOREIGN KEY (memberID) REFERENCES Member(memberID)," +
                "FOREIGN KEY (bookID) REFERENCES Book(bookID)" +
                ")"
        db?.execSQL(libraryLoanSlip)

        //create 3 data for loan slip
        db?.execSQL("INSERT INTO LibraryLoanSlip VALUES(1,'admin',1,1,'2020-12-12', 1)")
        db?.execSQL("INSERT INTO LibraryLoanSlip VALUES(2,'admin',2,2,'2020-12-12', 0)")
        db?.execSQL("INSERT INTO LibraryLoanSlip VALUES(3,'admin',3,3,'2020-12-12', 1)")

    }

    override fun onUpgrade(
        db: android.database.sqlite.SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $BOOK_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $MEMBER_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $LIBRARIAN_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $LOAN_SLIP_TABLE_NAME")
        onCreate(db)
    }
}