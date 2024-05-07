"use client";

import Footer from "@/components/Footer";
import Header from "@/components/Header";
import ScrollToTop from "@/components/ScrollToTop";
import { Inter } from "next/font/google";
import "../styles/index.css";
import { Providers } from "./providers";
import React, { useState } from "react";
import { getSession, SessionProvider } from "next-auth/react";
import RefreshTokenHandler from "@/components/RefreshTokenHandler/refreshTokenHandler";
import { getServerSession } from "next-auth";

const inter = Inter({ subsets: ["cyrillic"] });

export default function RootLayout({ children }: {
  children: React.ReactNode
}) {
  const [interval, setInterval] = useState(0);
  const session = getSession();

  return (
    <html suppressHydrationWarning lang="ru">

    <head><title></title></head>

    <body className={`bg-gray-light dark:bg-black flex flex-col min-h-screen ${inter.className}`}>
    <SessionProvider session={session} refetchInterval={interval}>
      <RefreshTokenHandler setInterval={setInterval} />
      <Providers>
        <Header />
        {children}
        <Footer />
        <ScrollToTop />
      </Providers>
    </SessionProvider>
    </body>
    </html>
  );
}

