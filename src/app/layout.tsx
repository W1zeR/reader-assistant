"use client";

import Footer from "@/components/Footer";
import Header from "@/components/Header";
import ScrollToTop from "@/components/ScrollToTop";
import { Inter } from "next/font/google";
import "../styles/index.css";
import { ThemeProvider } from "next-themes";
import React, { useState } from "react";
import RefreshTokenHandler from "@/components/RefreshTokenHandler/refreshTokenHandler";
import { SessionProvider } from "next-auth/react";

const inter = Inter({ subsets: ["cyrillic"] });

export default function RootLayout({ children }: {
  children: React.ReactNode
}) {
  const [interval, setInterval] = useState(0);

  return (
    <html suppressHydrationWarning lang="ru">

    <head><title></title></head>

    <body className={`bg-gray-light dark:bg-black flex flex-col min-h-screen ${inter.className}`}>
    <SessionProvider refetchInterval={interval}>
      <RefreshTokenHandler setInterval={setInterval} />
      <ThemeProvider attribute="class" enableSystem={false} defaultTheme="dark">
        <Header />
        {children}
        <Footer />
        <ScrollToTop />
      </ThemeProvider>
    </SessionProvider>
    </body>
    </html>
  );
}

