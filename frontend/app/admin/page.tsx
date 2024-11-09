import { UsersTable } from "@/components/UsersTable";
import { auth } from '@/auth'
import { redirect } from "next/navigation";
import { isAdmin } from "@/functions/user-management";

export default async function AdminPage() {
    const session = await auth();
    const admin = isAdmin(session);
    if (!session) {
        redirect('/login');
    }
    if (!admin) {
        redirect('/');
    }
    return (
        <div>
            <UsersTable />
        </div>
    )
};
