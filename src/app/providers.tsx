"use client";

import { ThemeProvider } from "next-themes";
import React, { useState } from "react";
import { SessionProvider } from "next-auth/react";
import RefreshTokenHandler from "@/components/RefreshTokenHandler/refreshTokenHandler";
import { Session } from "next-auth";

export function Providers({ children, session }: { children: React.ReactNode, session: Session }) {
  const [interval, setInterval] = useState(0);

  return (
    <SessionProvider session={session} refetchInterval={interval}>
      <RefreshTokenHandler setInterval={setInterval} />
      <ThemeProvider attribute="class" enableSystem={false} defaultTheme="dark">
        {children}
      </ThemeProvider>
    </SessionProvider>
  );
}
