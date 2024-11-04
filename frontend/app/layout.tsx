import "@/app/globals.css"
import type { Metadata } from "next";
import localFont from "next/font/local";
import { GeistSans } from "geist/font/sans"
import { GeistMono } from "geist/font/mono"
import NavBar from "./components/NavBar";

const geistSans = localFont({
  src: "./fonts/GeistVF.woff",
  variable: "--font-geist-sans",
  weight: "100 900",
});
const geistMono = localFont({
  src: "./fonts/GeistMonoVF.woff",
  variable: "--font-geist-mono",
  weight: "100 900",
});

export const metadata: Metadata = {
  title: "VirtualFridge",
  description: "Your fridge but better.",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en" suppressHydrationWarning>
      <head />
      <body className={`${GeistSans.variable} ${GeistMono.variable} font-sans antialiased`}>
        <NavBar />
        {children}
      </body>
    </html>
  )
}
